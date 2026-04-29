package com.app.ecomm_application.dto;

import com.app.ecomm_application.model.Product;
import com.app.ecomm_application.model.User;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CartItemResponse {
    private User user;
    private Product product;
    private Integer quantity;
    private BigDecimal price;
}
