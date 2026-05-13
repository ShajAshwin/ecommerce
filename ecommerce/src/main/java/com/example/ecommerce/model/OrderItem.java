package com.example.ecommerce.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int orderItemId;

    private int quantity;
    private double price;

    @ManyToOne
    @JoinColumn(name = "order_id")
    @JsonBackReference("order-items")       // ← breaks Order↔OrderItem loop
    private Order order;

    @ManyToOne
    @JoinColumn(name = "product_id")
    @JsonManagedReference("orderitem-product")
    private Product product;
}

