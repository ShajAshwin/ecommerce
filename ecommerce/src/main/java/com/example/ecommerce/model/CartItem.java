package com.example.ecommerce.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// New class needed to track products in cart
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int cartItemId;

    private int quantity;
    private double price;

    @ManyToOne
    @JoinColumn(name = "cart_id")
    @JsonBackReference("cart-items")        // ← breaks Cart↔CartItem loop
    private Cart cart;

    @ManyToOne
    @JoinColumn(name = "product_id")
    @JsonManagedReference("cartitem-product")
    private Product product;
}