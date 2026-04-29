package com.app.ecomm_application.repository;

import com.app.ecomm_application.dto.CartItemResponse;
import com.app.ecomm_application.dto.OrderItemDTO;
import com.app.ecomm_application.dto.OrderResponse;
import com.app.ecomm_application.model.*;
import com.app.ecomm_application.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final CartService cartService;
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;

    public Optional<OrderResponse> createOrder(String userId) {

//        Validate for cart items
        List<CartItemResponse> cartItemResponses= cartService.fetchCart(userId);
        if(cartItemResponses.isEmpty()){
            return Optional.empty();
        }

//        Validate for users
        Optional<User> userOptional = userRepository.findById(Long.valueOf(userId));
        if(userOptional.isEmpty()){
           return Optional.empty();
        }

        User user = userOptional.get();

//        Calculate total price
        BigDecimal totalPrice= cartItemResponses
                .stream()
                .map(CartItemResponse::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

//        Create order
        Order order=new Order();
        order.setUser(user);
        order.setTotalAmount(totalPrice);
        order.setStatus(OrderStatus.CONFIRMED);

        List<OrderItem> orderItems = cartItemResponses
                .stream()
                .map(item-> new OrderItem(
                        null,
                        item.getProduct(),
                        item.getQuantity(),
                        item.getPrice(),
                        order
                ))
                .toList();
        order.setItems(orderItems);

        Order savedOrder=orderRepository.save(order);

//        Clear cart
        cartService.clearCart(userId);

        return Optional.of(mapToOrderResponse(savedOrder));
    }

    private OrderResponse mapToOrderResponse(Order order) {
        return new OrderResponse(
                order.getId(),
                order.getTotalAmount(),
                order.getStatus(),
                order.getItems()
                        .stream()
                        .map(item-> new OrderItemDTO(
                                item.getId(),
                                item.getProduct().getId(),
                                item.getQuantity(),
                                item.getPrice(),
                                item.getPrice().multiply(new BigDecimal(item.getQuantity()))
                                )
                        )
                        .toList(),
                order.getCreatedAt()
        );
    }
}
