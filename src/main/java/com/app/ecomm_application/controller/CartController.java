package com.app.ecomm_application.controller;

import com.app.ecomm_application.dto.CartItemRequest;
import com.app.ecomm_application.dto.CartItemResponse;
import com.app.ecomm_application.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @PostMapping
    public ResponseEntity<String> addToCart(
            @RequestHeader("X-User-ID") String userId,
            @RequestBody CartItemRequest cartItemRequest
            ){
        if(!cartService.addToCart(userId, cartItemRequest)){
            return ResponseEntity
                    .badRequest()
                    .body("Product out of stock or user not found or product not found");
        }
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .build();
    }

    @DeleteMapping("/items/{productId}")
    public ResponseEntity<String> deleteFromCart(
            @RequestHeader("X-User-ID") String userId,
            @PathVariable Long productId
    ){
        return cartService.deleteItemFromCart(userId, productId)
                ?  ResponseEntity.noContent().build()
                :  ResponseEntity.badRequest().build();
    }

    @GetMapping
    public ResponseEntity<List<CartItemResponse>> fetchCart(
            @RequestHeader("X-User-ID") String userId
    ){
        List<CartItemResponse> cartItems = cartService.fetchCart(userId);
        return cartItems.isEmpty()
                ? ResponseEntity.notFound().build()
                : ResponseEntity.ok(cartItems);
    }
}
