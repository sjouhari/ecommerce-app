package com.ecommerce.order.service.impl;

import com.ecommerce.order.dto.*;
import com.ecommerce.order.entity.*;
import com.ecommerce.order.enums.FactureStatus;
import com.ecommerce.order.enums.OrderStatus;
import com.ecommerce.order.enums.PaymentMethods;
import com.ecommerce.order.enums.PaymentMethodStatus;
import com.ecommerce.order.exception.InvalidPaymentMethodException;
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
import com.ecommerce.shared.dto.StoreDto;
import com.ecommerce.shared.dto.UserEvent;
import com.ecommerce.shared.exception.ResourceNotFoundException;
import com.ecommerce.shared.exception.StockInsufficientException;
import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.text.pdf.draw.LineSeparator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final PaymentMethodRepository paymentMethodRepository;
    private final AddressRepository addressRepository;
    private final UserApiClient userApiClient;
    private final InventoryApiClient inventoryApiClient;
    private final ProductApiClient productApiClient;
    private final OrderPlacedProducer orderPlacedProducer;

    @Value("${kafka.topic.order.placed.name}")
    private String orderPlacedTopic;

    @Value("${kafka.topic.order.status.changed.name}")
    private String orderStatusTopic;

    public OrderServiceImpl(OrderRepository orderRepository, OrderItemRepository orderItemRepository, PaymentMethodRepository paymentMethodRepository, AddressRepository addressRepository, UserApiClient userApiClient, InventoryApiClient inventoryApiClient, ProductApiClient productApiClient, OrderPlacedProducer orderPlacedProducer) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.paymentMethodRepository = paymentMethodRepository;
        this.addressRepository = addressRepository;
        this.userApiClient = userApiClient;
        this.inventoryApiClient = inventoryApiClient;
        this.productApiClient = productApiClient;
        this.orderPlacedProducer = orderPlacedProducer;
    }

    @Override
    public List<OrderResponseDto> getAllOrders(String token) {
        List<Order> orders = orderRepository.findAll();

        return orders.stream()
                .map(order -> {
                    UserDto userDto = userApiClient.getUserById(order.getUserId(), token);
                    return getOrderResponseDto(order, userDto.getFirstName() + " " + userDto.getLastName());
                })
                .toList();
    }

    @Override
    public List<OrderResponseDto> getOrdersByUserId(Long userId, String token) {
        List<Order> orders = orderRepository.findAllByUserIdOrderByCreatedAtDesc(userId);
        return orders.stream()
                .map(order -> {
                    UserDto userDto = userApiClient.getUserById(order.getUserId(), token);
                    return getOrderResponseDto(order, userDto.getFirstName() + " " + userDto.getLastName());
                })
                .toList();
    }

    @Override
    public List<OrderResponseDto> getOrdersByStoreId(Long storeId, String token) {
        List<Order> orders = orderRepository.findAllByStoreIdOrderByCreatedAtDesc(storeId);
        return orders.stream()
                .map(order -> {
                    UserDto userDto = userApiClient.getUserById(order.getUserId(), token);
                    return getOrderResponseDto(order, userDto.getFirstName() + " " + userDto.getLastName());
                })
                .toList();
    }

    @Override
    public OrderResponseDto getOrderById(Long orderId, String token) {
        Order order = orderRepository.findById(orderId).orElseThrow(
                () -> new ResourceNotFoundException("Order", "id", orderId.toString())
        );
        UserDto userDto = userApiClient.getUserById(order.getUserId(), token);
        return getOrderResponseDto(order, userDto.getFirstName() + " " + userDto.getLastName());
    }

    @Override
    public void placeOrder(OrderRequestDto orderRequestDto, String token) {

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

        // Group order items by store id
        Map<Long, List<OrderItem>> orderItemsMap = new HashMap<>();
        orderItems.forEach(orderItem -> {
            ProductDto productDto = productApiClient.getProductById(orderItem.getProductId());
            Long storeId = productDto.getStore().getId();
            if(!orderItemsMap.containsKey(storeId)) {
                orderItemsMap.put(storeId, new java.util.ArrayList<>());
            }
            orderItemsMap.get(storeId).add(orderItem);
        });

        // Create New Payment Method
        PaymentMethod paymentMethod = getPaymentMethod(orderRequestDto);
        paymentMethod.setStatus(PaymentMethodStatus.PENDING);

        // Set delivery address
        Address address = addressRepository.findById(orderRequestDto.getDeliveryAddressId()).orElseThrow(
                () -> new ResourceNotFoundException("Address", "id", orderRequestDto.getDeliveryAddressId().toString())
        );

        orderItemsMap.forEach((storeId, items) -> {
            // Create new Order
            Order order = new Order();
            order.setUserId(orderRequestDto.getUserId());
            order.setStoreId(storeId);
            order.setStatus(OrderStatus.PENDING);
            order.setOrderItems(items);

            // Create new Facture
            Invoice invoice = new Invoice();
            invoice.setPaymentMethod(paymentMethod);
            invoice.setTotalPrice(calculateTotalPrice(order.getOrderItems()));
            invoice.setOrder(order);
            order.setInvoice(invoice);
            order.setDeliveryAddress(address);

            // Save Order
            Order savedOrder = orderRepository.save(order);
            order.getOrderItems().forEach(orderItem -> {
                orderItem.setOrder(savedOrder);
                orderItem.setShoppingCart(null);
                orderItemRepository.save(orderItem);
            });

            // Deduct stock for each order item
            items.forEach(orderItem -> {
                InventoryDto inventoryDto = OrderItemMapper.INSTANCE.orderItemToInventoryDto(orderItem);
                inventoryApiClient.deductQuantity(inventoryDto, token);
            });

            // Send order placed event
            OrderEvent orderEvent = new OrderEvent();
            orderEvent.setOrderId(savedOrder.getId());
            orderEvent.setUser(new UserEvent(userDto.getFirstName() + " " + userDto.getLastName(), userDto.getEmail(), 0));
            orderEvent.setStatus(savedOrder.getStatus().toString());
            orderPlacedProducer.sendMessage(orderEvent, orderPlacedTopic);
        });
    }

    @Override
    public OrderResponseDto updateOrderStatus(Long orderId, UpdateOrderStatusRequestDto updateOrderStatusRequestDto, String token) {
        Order order = orderRepository.findById(orderId).orElseThrow(
                () -> new ResourceNotFoundException("Order", "id", orderId.toString())
        );
        order.setStatus(updateOrderStatusRequestDto.getStatus());
        if(updateOrderStatusRequestDto.getStatus() == OrderStatus.DELIVERED) {
            order.setDeliveryDate(LocalDateTime.now());
            Invoice invoice = order.getInvoice();
            invoice.setStatus(FactureStatus.PAID);
            PaymentMethod paymentMethod = invoice.getPaymentMethod();
            paymentMethod.setStatus(PaymentMethodStatus.PAID);
            invoice.setPaymentMethod(paymentMethod);
            order.setInvoice(invoice);
        }
        Order savedOrder = orderRepository.save(order);
        // Send order placed event
        UserDto userDto = userApiClient.getUserById(order.getUserId(), token);
        OrderEvent orderEvent = new OrderEvent();
        orderEvent.setOrderId(savedOrder.getId());
        String userName = userDto.getFirstName() + " " + userDto.getLastName();
        orderEvent.setUser(new UserEvent(userName, userDto.getEmail(), 0));
        orderEvent.setStatus(savedOrder.getStatus().toString());
        orderPlacedProducer.sendMessage(orderEvent, orderStatusTopic);
        return getOrderResponseDto(savedOrder, userName);
    }

    private OrderResponseDto getOrderResponseDto(Order order, String userName) {
        StoreDto storeDto = userApiClient.getStoreById(order.getStoreId());
        OrderResponseDto orderResponseDto = OrderMapper.INSTANCE.orderToOrderResponseDto(order);
        orderResponseDto.setUserName(userName);
        orderResponseDto.setStoreName(storeDto.getName());
        orderResponseDto.getOrderItems().forEach(orderItem -> {
            ProductDto productDto = productApiClient.getProductById(orderItem.getProductId());
            orderItem.setProductName(productDto.getName());
            orderItem.setProductImage(productDto.getMedias().getFirst().getUrl());
        });
        return orderResponseDto;
    }

    @Override
    public void confirmOrderPayement(Long paymentMethodId) {
        PaymentMethod paymentMethod = paymentMethodRepository.findById(paymentMethodId).orElseThrow(
                () -> new ResourceNotFoundException("Payment Method", "id", paymentMethodId.toString())
        );

        paymentMethod.setStatus(PaymentMethodStatus.PAID);
        paymentMethodRepository.save(paymentMethod);
    }

    @Override
    public String deleteOrder(Long orderId) {
        orderRepository.findById(orderId).orElseThrow(
                () -> new ResourceNotFoundException("Order", "id", orderId.toString())
        );
        orderRepository.deleteById(orderId);
        return "Order deleted successfully";
    }

    @Override
    public List<BestSellingProductDto> getBestSellingProducts() {
        return getBestSellingProductDtos(orderItemRepository.findBestSellingProducts());
    }

    @Override
    public List<BestSellingProductDto> getBestSellingProductsByStoreId(Long storeId) {
        return getBestSellingProductDtos(orderItemRepository.findBestSellingProductsByStoreId(storeId));
    }

    @Override
    public List<TopSellersDto> getTopSellers() {
        return orderItemRepository.findTopSellers().stream()
                .map(topSellersProjection -> {
                    StoreDto storeDto = userApiClient.getStoreById(topSellersProjection.getStoreId());
                    TopSellersDto topSellersDto = new TopSellersDto();
                    topSellersDto.setStoreId(topSellersProjection.getStoreId());
                    topSellersDto.setStoreName(storeDto.getName());
                    topSellersDto.setTotalSold(topSellersProjection.getTotalSold());
                    return topSellersDto;
                }).toList();
    }

    @Override
    public byte[] generateOrderPdf(Long orderId, String token) throws IOException {
        OrderResponseDto order = getOrderById(orderId, token);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Document document = new Document();
        PdfWriter.getInstance(document, baos);

        Font paragraphFont = new Font(Font.COURIER, 11);

        AddressDto addressDto = order.getDeliveryAddress();
        String deliveryAddress = addressDto.getDeliveryAddress() +  ", " + addressDto.getPostalCode() + ", " + addressDto.getCity() + ", " + addressDto.getCountry();

        document.open();
        Image logo = Image.getInstance("classpath:/static/logo.png");
        logo.scaleToFit(120, 120);
        logo.setAlignment(Element.ALIGN_LEFT);
        document.add(logo);
        document.add(Chunk.NEWLINE);
        document.add(new LineSeparator());
        document.add(Chunk.NEWLINE);

        document.add(new Paragraph("Commande N°: " + order.getId()));
        document.add(Chunk.NEWLINE);
        document.add(new LineSeparator());

        document.add(new Paragraph("Client               : " + order.getUserName(), paragraphFont));
        document.add(new Paragraph("Date                 : " + order.getCreatedAt(), paragraphFont));
        document.add(new Paragraph("Vendeur              : " + order.getStoreName(), paragraphFont));
        document.add(new Paragraph("Adresse de livraison : " + deliveryAddress, paragraphFont));
        document.add(new Paragraph("Statut               : " + order.getStatus(), paragraphFont));

        document.add(Chunk.NEWLINE);
        document.add(new Paragraph("Listes des produits commandés : "));
        document.add(Chunk.NEWLINE);
        document.add(new LineSeparator());

        for(OrderItemRequestDto item : order.getOrderItems()) {
            document.add(new Paragraph("Produit #" + item.getProductId(), paragraphFont));
            document.add(new Paragraph("Nom        : " + item.getProductName(), paragraphFont));
            document.add(new Paragraph("Détails    : " + item.getSize() + ", " + item.getColor() + ", " + item.getPrice() + ", x" + item.getQuantity() + ", " + " MAD", paragraphFont));
            document.add(new Paragraph("Prix total : " + (item.getPrice() * item.getQuantity()) + " MAD", paragraphFont));
            document.add(Chunk.NEWLINE);
            document.add(new LineSeparator());
        }

        document.add(Chunk.NEWLINE);
        document.add(new Paragraph("Résumé de la commande : "));
        document.add(new Paragraph("Prix Total         : " + order.getInvoice().getTotalPrice() + " MAD", paragraphFont));
        document.add(new Paragraph("Statut de paiement : " + order.getInvoice().getPaymentMethod().getStatus().toString(), paragraphFont));
        document.close();

        return baos.toByteArray();
    }

    private List<BestSellingProductDto> getBestSellingProductDtos(List<BestSellingProductProjection> bestSellingProductProjections) {
        return bestSellingProductProjections.stream()
                .map(bestSellingProductProjection -> {
                    ProductDto productDto = productApiClient.getProductById(bestSellingProductProjection.getProductId());
                    BestSellingProductDto bestSellingProductDto = new BestSellingProductDto();
                    bestSellingProductDto.setProductId(bestSellingProductProjection.getProductId());
                    bestSellingProductDto.setTotalSold(bestSellingProductProjection.getTotalSold());
                    bestSellingProductDto.setProductName(productDto.getName());
                    bestSellingProductDto.setProductImage(productDto.getMedias().getFirst().getUrl());
                    return bestSellingProductDto;
                }).toList();
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
        throw new InvalidPaymentMethodException("Invalid payment method");
    }
}
