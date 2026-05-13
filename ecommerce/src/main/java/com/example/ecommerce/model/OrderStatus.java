package com.example.ecommerce.model;

// Move enum OUTSIDE of Order class as a separate file
public enum OrderStatus {
    PENDING,
    SHIPPED,
    DELIVERED,
    CANCELED
}