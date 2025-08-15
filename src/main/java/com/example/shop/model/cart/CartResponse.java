package com.example.shop.model.cart;

import com.example.shop.model.product.ProductResponse;
import com.example.shop.model.user.UserResponse;
import lombok.Data;

@Data
public class CartResponse {
    private Long id;
    private UserResponse user;
    private ProductResponse product;
    private int quantity;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserResponse getUser() {
        return user;
    }

    public void setUser(UserResponse user) {
        this.user = user;
    }

    public ProductResponse getProduct() {
        return product;
    }

    public void setProduct(ProductResponse product) {
        this.product = product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
