package com.ecommerce.order.service.impl;

import com.ecommerce.order.dto.OrderItemDto;
import com.ecommerce.order.dto.SelectOrderItemDto;
import com.ecommerce.order.dto.ShoppingCartDto;
import com.ecommerce.order.dto.UpdateOrderItemQauntityDto;
import com.ecommerce.order.entity.OrderItem;
import com.ecommerce.order.entity.ShoppingCart;
import com.ecommerce.order.mapper.OrderItemMapper;
import com.ecommerce.order.mapper.ShoppingCartMapper;
import com.ecommerce.order.repository.OrderItemRepository;
import com.ecommerce.order.repository.ShoppingCartRepository;
import com.ecommerce.order.service.ShoppingCartService;
import com.ecommerce.shared.exception.ResourceNotFoundException;
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

    @Override
    public ShoppingCartDto getShoppingCartByUserId(Long userId) {
        Optional<ShoppingCart> shoppingCart = shoppingCartRepository.findByUserId(userId);
        return shoppingCart.map(ShoppingCartMapper.INSTANCE::shoppingCartToShoppingCartDto).orElse(null);
    }

    @Override
    public ShoppingCartDto addItemToShoppingCart(Long userId, OrderItemDto orderItemDto) {
        Optional<ShoppingCart> shoppingCart = shoppingCartRepository.findByUserId(userId);
        ShoppingCart cart = new ShoppingCart();
        if (shoppingCart.isPresent()) {
            cart = shoppingCart.get();
            Optional<OrderItem> item = cart.getOrderItems().stream()
                    .filter(orderItem ->
                            Objects.equals(orderItem.getProductId(), orderItemDto.getProductId())
                                    && Objects.equals(orderItem.getSize(), orderItemDto.getSize())
                                    && Objects.equals(orderItem.getColor(), orderItemDto.getColor()))
                    .findFirst();
            if (item.isPresent()) {
                OrderItem orderItem = item.get();
                updateItemQuantity(orderItem.getId(), new UpdateOrderItemQauntityDto(orderItemDto.getQuantity()));
                return getShoppingCartByUserId(userId);
            }
        } else {
            cart.setUserId(userId);
        }
        if(cart.getOrderItems() == null) {
            cart.setOrderItems(new java.util.ArrayList<>());
        }
        OrderItem orderItem = OrderItemMapper.INSTANCE.orderItemDtoToOrderItem(orderItemDto);
        cart.getOrderItems().add(orderItem);
        orderItem.setShoppingCart(cart);
        return ShoppingCartMapper.INSTANCE.shoppingCartToShoppingCartDto(shoppingCartRepository.save(cart));
    }

    @Override
    public OrderItemDto updateItemQuantity(Long id, UpdateOrderItemQauntityDto updateOrderItemQauntityDto) {
        OrderItem orderItem = orderItemRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Order Item", "id", id.toString())
        );
        orderItem.setQuantity(updateOrderItemQauntityDto.getQuantity());
        OrderItem savedOrderItem = orderItemRepository.save(orderItem);
        return OrderItemMapper.INSTANCE.orderItemToOrderItemDto(savedOrderItem);
    }

    @Override
    public OrderItemDto selectOrderItem(Long id, SelectOrderItemDto selectOrderItemDto) {
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
