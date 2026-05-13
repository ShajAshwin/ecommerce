package com.example.ecommerce.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int orderId;

    private LocalDate orderDate;
    private double totalAmount;

    @Enumerated(EnumType.STRING)                 // saves "SHIPPED" not 0,1,2
    private OrderStatus orderStatus;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonBackReference("user-orders")       // ← breaks User↔Order loop
    private User user;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference("order-items")
    private List<OrderItem> orderItems = new ArrayList<>();
}
