package com.ecommerce.order.service.impl;

import com.ecommerce.order.dto.OrderRequestDto;
import com.ecommerce.order.dto.OrderResponseDto;
import com.ecommerce.order.dto.UpdateOrderStatusRequestDto;
import com.ecommerce.order.dto.UserDto;
import com.ecommerce.order.entity.*;
import com.ecommerce.order.enums.OrderStatus;
import com.ecommerce.order.enums.PaymentMethods;
import com.ecommerce.order.enums.PaymentMethodStatus;
import com.ecommerce.order.kafka.OrderPlacedProducer;
import com.ecommerce.order.mapper.OrderItemMapper;
import com.ecommerce.order.mapper.OrderMapper;
import com.ecommerce.order.repository.AddressRepository;
import com.ecommerce.order.repository.OrderItemRepository;
import com.ecommerce.order.repository.OrderRepository;
import com.ecommerce.order.repository.PaymentMethodRepository;
import com.ecommerce.order.service.OrderService;
import com.ecommerce.shared.dto.InventoryDto;
import com.ecommerce.shared.dto.OrderEvent;
import com.ecommerce.shared.dto.UserEvent;
import com.ecommerce.shared.exception.ResourceNotFoundException;
import com.ecommerce.shared.exception.StockInsufficientException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private PaymentMethodRepository paymentMethodRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private UserApiClient userApiClient;

    @Autowired
    private InventoryApiClient inventoryApiClient;

    @Autowired
    private OrderPlacedProducer orderPlacedProducer;

    @Value("${kafka.topic.order.placed.name}")
    private String orderPlacedTopic;

    @Value("${kafka.topic.order.status.changed.name}")
    private String orderStatusTopic;

    @Override
    public List<OrderResponseDto> getAllOrders() {
        List<Order> orders = orderRepository.findAll();
        return OrderMapper.INSTANCE.ordersToOrderResponseDtos(orders);
    }

    @Override
    public List<OrderResponseDto> getOrdersByUserId(Long userId) {
        List<Order> orders = orderRepository.findAllByUserId(userId);
        return OrderMapper.INSTANCE.ordersToOrderResponseDtos(orders);
    }

    @Override
    public OrderResponseDto getOrderById(Long orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(
                () -> new ResourceNotFoundException("Order", "id", orderId.toString())
        );
        return OrderMapper.INSTANCE.orderToOrderResponseDto(order);
    }

    @Override
    public OrderResponseDto placeOrder(OrderRequestDto orderRequestDto, String token) {

        // Verify user id
        UserDto userDto = userApiClient.getUserById(orderRequestDto.getUserId(), token);

        // Get order items
        List<OrderItem> orderItems = orderItemRepository.findAllById(orderRequestDto.getOrderItemsIds());

        // Check stock availability for each order item
        orderItems.forEach(orderItem -> {
            InventoryDto inventoryDto = OrderItemMapper.INSTANCE.orderItemToInventoryDto(orderItem);
            if(!inventoryApiClient.checkAvailability(inventoryDto, token)) {
                throw new StockInsufficientException(orderItem.getProductId(), orderItem.getSize(), orderItem.getColor().toString());
            }
        });

        // Create new Order
        Order order = new Order();
        order.setUserId(orderRequestDto.getUserId());
        order.setStatus(OrderStatus.PENDING);
        order.setOrderItems(orderItems);

        // Create New Payment Method
        PaymentMethod paymentMethod = getPaymentMethod(orderRequestDto);
        paymentMethod.setStatus(PaymentMethodStatus.PENDING);

        // Create new Facture
        Invoice invoice = new Invoice();
        invoice.setPaymentMethod(paymentMethod);
        invoice.setTotalPrice(calculateTotalPrice(order.getOrderItems()));
        invoice.setOrder(order);
        order.setInvoice(invoice);

        // Set delivery address
        Address address = addressRepository.findById(orderRequestDto.getDeliveryAddressId()).orElseThrow(
                () -> new ResourceNotFoundException("Address", "id", orderRequestDto.getDeliveryAddressId().toString())
        );
        order.setDeliveryAddress(address);

        // Save Order
        Order savedOrder = orderRepository.save(order);
        order.getOrderItems().forEach(orderItem -> {
            orderItem.setOrder(savedOrder);
            orderItem.setShoppingCart(null);
            orderItemRepository.save(orderItem);
        });

        // Deduct stock for each order item
        orderItems.forEach(orderItem -> {
            InventoryDto inventoryDto = OrderItemMapper.INSTANCE.orderItemToInventoryDto(orderItem);
            inventoryApiClient.deductQuantity(inventoryDto, token);
        });

        // Send order placed event
        OrderEvent orderEvent = new OrderEvent();
        orderEvent.setOrderId(savedOrder.getId());
        orderEvent.setUser(new UserEvent(userDto.getFirstName() + " " + userDto.getLastName(), userDto.getEmail(), 0));
        orderEvent.setStatus(savedOrder.getStatus().toString());
        orderPlacedProducer.sendMessage(orderEvent, orderPlacedTopic);
        return OrderMapper.INSTANCE.orderToOrderResponseDto(savedOrder);
    }

    @Override
    public OrderResponseDto updateOrderStatus(Long orderId, UpdateOrderStatusRequestDto updateOrderStatusRequestDto, String token) {
        Order order = orderRepository.findById(orderId).orElseThrow(
                () -> new ResourceNotFoundException("Order", "id", orderId.toString())
        );
        order.setStatus(updateOrderStatusRequestDto.getStatus());
        Order savedOrder = orderRepository.save(order);
        // Send order placed event
        UserDto userDto = userApiClient.getUserById(order.getUserId(), token);
        OrderEvent orderEvent = new OrderEvent();
        orderEvent.setOrderId(savedOrder.getId());
        orderEvent.setUser(new UserEvent(userDto.getFirstName() + " " + userDto.getLastName(), userDto.getEmail(), 0));
        orderEvent.setStatus(savedOrder.getStatus().toString());
        orderPlacedProducer.sendMessage(orderEvent, orderStatusTopic);
        return OrderMapper.INSTANCE.orderToOrderResponseDto(savedOrder);
    }

    @Override
    public String deleteOrder(Long orderId) {
        orderRepository.findById(orderId).orElseThrow(
                () -> new ResourceNotFoundException("Order", "id", orderId.toString())
        );
        orderRepository.deleteById(orderId);
        return "Order deleted successfully";
    }

    private double calculateTotalPrice(List<OrderItem> orderItems) {
        return orderItems.stream()
                .mapToDouble(orderItem -> orderItem.getPrice() * orderItem.getQuantity())
                .sum();
    }

    private PaymentMethod getPaymentMethod(OrderRequestDto orderRequestDto) {
        if(orderRequestDto.getPaymentMethod() == PaymentMethods.CASH) {
            return new Cash();
        } else if(orderRequestDto.getPaymentMethod() == PaymentMethods.CHEQUE) {
            return new Cheque(orderRequestDto.getChequeNumber(), orderRequestDto.getBankName());
        }
        throw new RuntimeException("Invalid payment method");
    }
}
