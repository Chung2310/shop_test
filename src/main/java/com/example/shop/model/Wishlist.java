package com.example.shop.model;

import jakarta.persistence.*;

@Entity
@Table( uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "book_id"}))
public class Wishlist {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    private Product product;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Product getBook() {
        return product;
    }

    public void setBook(Product product) {
        this.product = product;
    }
}
