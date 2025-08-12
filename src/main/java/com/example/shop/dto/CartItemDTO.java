package com.example.shop.dto;

import lombok.Data;

@Data
public class CartItemDTO {
    private Long id;
    private ProductDTO book;
    private int quantity;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ProductDTO getBook() {
        return book;
    }

    public void setBook(ProductDTO book) {
        this.book = book;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
