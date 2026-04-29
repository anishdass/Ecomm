package com.app.ecomm_application.service;

import com.app.ecomm_application.dto.CartItemRequest;
import com.app.ecomm_application.dto.CartItemResponse;
import com.app.ecomm_application.model.CartItem;
import com.app.ecomm_application.model.Product;
import com.app.ecomm_application.model.User;
import com.app.ecomm_application.repository.CartItemRepository;
import com.app.ecomm_application.repository.ProductRepository;
import com.app.ecomm_application.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class CartService {
    private final ProductRepository productRepository;
    private final CartItemRepository cartItemRepository;
    private final UserRepository userRepository;

    public boolean addToCart(String userId, CartItemRequest cartItemRequest) {

        Optional<Product> productOpt=productRepository.findById(cartItemRequest.getProductId());
        if(productOpt.isEmpty()){
            return false;
        }
        Product product=productOpt.get();

        if(product.getStockQuantity() < cartItemRequest.getQuantity()){
            return false;
        }

        Optional<User> userOptional=userRepository.findById(Long.valueOf(userId));
        if(userOptional.isEmpty()){
            return false;
        }
        User user = userOptional.get();

        CartItem existingCartItem = cartItemRepository.findByUserAndProduct(user, product);

        if(existingCartItem!=null){
            existingCartItem
                    .setQuantity(existingCartItem.getQuantity() + cartItemRequest.getQuantity());

            existingCartItem
                    .setPrice(product.getPrice()
                            .multiply(BigDecimal.valueOf(cartItemRequest.getQuantity())));
        }else{
            CartItem cartItem=new CartItem();
            cartItem.setUser(user);
            cartItem.setProduct(product);
            cartItem.setPrice(product.getPrice()
                    .multiply(BigDecimal.valueOf(cartItemRequest.getQuantity())));
            cartItem.setQuantity(cartItemRequest.getQuantity());
            cartItemRepository.save(cartItem);
        }

        return true;
    }

    public boolean deleteItemFromCart(String userId, Long productId) {
        Optional<Product> productOpt=productRepository.findById(productId);
        Optional<User> userOpt=userRepository.findById(Long.valueOf(userId));

        if(userOpt.isPresent() && productOpt.isPresent()){
            cartItemRepository.deleteByUserAndProduct(userOpt.get(), productOpt.get());
            return true;
        }
        return false;
    }


    public List<CartItemResponse> fetchCart(String userId) {

        return userRepository.findById(Long.valueOf(userId))
                .map(cartItemRepository::findByUser)
                .stream()
                .flatMap(List::stream)
                .map(this::mapToCartItemResponse)
                .collect(Collectors.toList());
    }

    private CartItemResponse mapToCartItemResponse(CartItem cartItem) {
        CartItemResponse cartItemResponse=new CartItemResponse();
        cartItemResponse.setUser(cartItem.getUser());
        cartItemResponse.setProduct(cartItem.getProduct());
        cartItemResponse.setQuantity(cartItem.getQuantity());
        cartItemResponse.setPrice(cartItem.getPrice());
        return cartItemResponse;
    }

    public void clearCart(String userId) {
        userRepository.findById(Long.valueOf(userId)).ifPresent(
                cartItemRepository::deleteByUser
        );
    }
}
