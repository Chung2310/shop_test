package com.example.shop.model.wishlist;

import com.example.shop.model.product.Product;
import com.example.shop.model.user.UserEntity;
import jakarta.persistence.*;

@Entity
@Table( uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "book_id"}))
public class Wishlist {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private UserEntity userEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    private Product product;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserEntity getUser() {
        return userEntity;
    }

    public void setUser(UserEntity userEntity) {
        this.userEntity = userEntity;
    }

    public Product getBook() {
        return product;
    }

    public void setBook(Product product) {
        this.product = product;
    }
}
