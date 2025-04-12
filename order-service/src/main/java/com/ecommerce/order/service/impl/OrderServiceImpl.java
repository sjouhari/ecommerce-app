package com.ecommerce.order.service.impl;

import com.ecommerce.order.dto.OrderRequestDto;
import com.ecommerce.order.dto.OrderResponseDto;
import com.ecommerce.order.dto.UpdateOrderStatusRequestDto;
import com.ecommerce.order.dto.UserDto;
import com.ecommerce.order.entity.Facture;
import com.ecommerce.order.entity.ModePayment;
import com.ecommerce.order.entity.Order;
import com.ecommerce.order.entity.OrderItem;
import com.ecommerce.order.enums.FactureStatus;
import com.ecommerce.order.enums.OrderStatus;
import com.ecommerce.order.exception.ResourceNotFoundException;
import com.ecommerce.order.kafka.OrderPlacedProducer;
import com.ecommerce.order.mapper.OrderMapper;
import com.ecommerce.order.repository.ModePaymentRepository;
import com.ecommerce.order.repository.OrderRepository;
import com.ecommerce.order.service.OrderService;
import com.ecommerce.shared.dto.InventoryDto;
import com.ecommerce.shared.dto.OrderEvent;
import com.ecommerce.shared.dto.UserEvent;
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
    private ModePaymentRepository modePaymentRepository;

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
        UserDto userDto = userApiClient.getUserById(orderRequestDto.getUserId(), token);
        orderRequestDto.getOrderItems().forEach(orderItem -> {
            InventoryDto inventoryDto = new InventoryDto();
            inventoryDto.setProductId(orderItem.getProductId());
            inventoryDto.setSize(orderItem.getSize());
            inventoryDto.setColor(orderItem.getColor());
            inventoryDto.setQuantity(orderItem.getQuantity());
            if(!inventoryApiClient.checkAvailability(inventoryDto, token)) {
                throw new StockInsufficientException(orderItem.getProductId(), orderItem.getSize(), orderItem.getColor().toString());
            }
        });
        Order order = OrderMapper.INSTANCE.orderRequestDtoToOrder(orderRequestDto);
        order.setStatus(OrderStatus.PENDING);
        order.getDeliveryAddress().setOrder(order);
        order.getOrderItems().forEach(orderItem -> orderItem.setOrder(order));

        ModePayment modePayment = modePaymentRepository.findByName(orderRequestDto.getModePayment()).orElseThrow(
                () -> new ResourceNotFoundException("ModePayment", "name", orderRequestDto.getModePayment())
        );

        Facture facture = new Facture();
        facture.setModePayment(modePayment);
        facture.setTotalPrice(calculateTotalPrice(order.getOrderItems()));
        facture.setStatus(FactureStatus.UNPAID);
        facture.setOrder(order);
        order.setFacture(facture);
        Order savedOrder = orderRepository.save(order);

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
        double totalPrice = 0.0;
        totalPrice = orderItems.stream()
                .mapToDouble(orderItem -> orderItem.getPrice() * orderItem.getQuantity())
                .sum();
        return totalPrice;
    }
}
