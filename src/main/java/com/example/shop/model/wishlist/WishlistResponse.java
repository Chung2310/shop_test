package com.example.shop.model.wishlist;

import com.example.shop.model.product.Product;
import com.example.shop.model.product.ProductResponse;
import com.example.shop.model.user.UserResponse;

public class WishlistResponse {
    private Long id;
    private UserResponse userResponse;
    private ProductResponse productResponse;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserResponse getUserResponse() {
        return userResponse;
    }

    public void setUserResponse(UserResponse userResponse) {
        this.userResponse = userResponse;
    }

    public ProductResponse getProductResponse() {
        return productResponse;
    }

    public void setProductResponse(ProductResponse productResponse) {
        this.productResponse = productResponse;
    }
}
