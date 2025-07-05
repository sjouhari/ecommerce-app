package com.ecommerce.order.service.impl;

import com.ecommerce.order.dto.*;
import com.ecommerce.order.entity.OrderItem;
import com.ecommerce.order.entity.ShoppingCart;
import com.ecommerce.order.mapper.OrderItemMapper;
import com.ecommerce.order.mapper.ShoppingCartMapper;
import com.ecommerce.order.repository.OrderItemRepository;
import com.ecommerce.order.repository.ShoppingCartRepository;
import com.ecommerce.order.service.ShoppingCartService;
import com.ecommerce.shared.exception.ResourceNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {

    @Autowired
    private ShoppingCartRepository shoppingCartRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private ProductApiClient productApiClient;

    @Override
    public ShoppingCartDto getShoppingCartByUserId(Long userId) {
        Optional<ShoppingCart> shoppingCart = shoppingCartRepository.findByUserId(userId);
        if (shoppingCart.isEmpty()) {
            throw new ResourceNotFoundException("Shopping cart", "userId", userId.toString());
        }
        ShoppingCartDto shoppingCartDto = ShoppingCartMapper.INSTANCE.shoppingCartToShoppingCartDto(shoppingCart.get());
        shoppingCartDto.setOrderItems(shoppingCartDto.getOrderItems().stream()
                .map(orderItem -> {
                    return getOrderItemRequestDto(orderItem);
                }).toList());
        return shoppingCartDto;
    }

    private OrderItemRequestDto getOrderItemRequestDto(OrderItemRequestDto orderItem) {
        ProductDto productDto = productApiClient.getProductById(orderItem.getProductId());
        orderItem.setProductName(productDto.getName());
        orderItem.setProductImage(productDto.getMedias().getFirst().getUrl());
        return orderItem;
    }

    @Override
    public ShoppingCartDto addItemToShoppingCart(Long userId, OrderItemRequestDto orderItemRequestDto) {
        Optional<ShoppingCart> shoppingCart = shoppingCartRepository.findByUserId(userId);
        ShoppingCart cart = new ShoppingCart();
        if (shoppingCart.isPresent()) {
            cart = shoppingCart.get();
            Optional<OrderItem> item = cart.getOrderItems().stream()
                    .filter(orderItem ->
                            Objects.equals(orderItem.getProductId(), orderItemRequestDto.getProductId())
                                    && Objects.equals(orderItem.getSize(), orderItemRequestDto.getSize())
                                    && Objects.equals(orderItem.getColor(), orderItemRequestDto.getColor()))
                    .findFirst();
            if (item.isPresent()) {
                OrderItem orderItem = item.get();
                updateItemQuantity(orderItem.getId(), new UpdateOrderItemQauntityDto(orderItemRequestDto.getQuantity()));
                return getShoppingCartByUserId(userId);
            }
        } else {
            cart.setUserId(userId);
        }
        if(cart.getOrderItems() == null) {
            cart.setOrderItems(new java.util.ArrayList<>());
        }
        OrderItem orderItem = OrderItemMapper.INSTANCE.orderItemDtoToOrderItem(orderItemRequestDto);
        cart.getOrderItems().add(orderItem);
        orderItem.setShoppingCart(cart);
        return ShoppingCartMapper.INSTANCE.shoppingCartToShoppingCartDto(shoppingCartRepository.save(cart));
    }

    @Override
    @Transactional
    public void clearUserCart(Long userId) {
        shoppingCartRepository.deleteByUserId(userId);
    }

    @Override
    public OrderItemRequestDto updateItemQuantity(Long id, UpdateOrderItemQauntityDto updateOrderItemQauntityDto) {
        OrderItem orderItem = orderItemRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Order Item", "id", id.toString())
        );
        orderItem.setQuantity(updateOrderItemQauntityDto.getQuantity());
        OrderItem savedOrderItem = orderItemRepository.save(orderItem);
        return OrderItemMapper.INSTANCE.orderItemToOrderItemDto(savedOrderItem);
    }

    @Override
    public OrderItemRequestDto selectOrderItem(Long id, SelectOrderItemDto selectOrderItemDto) {
        OrderItem orderItem = orderItemRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Order Item", "id", id.toString())
        );
        orderItem.setSelected(selectOrderItemDto.isSelected());
        OrderItem savedOrderItem = orderItemRepository.save(orderItem);
        return OrderItemMapper.INSTANCE.orderItemToOrderItemDto(savedOrderItem);
    }

    @Override
    public void deleteItemFromShoppingCart(Long id) {
        orderItemRepository.deleteById(id);
    }

}
