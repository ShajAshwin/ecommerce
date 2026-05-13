package com.example.ecommerce.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int categoryId;

    @Column(nullable = false, unique = true)
    private String categoryName;

    @ManyToMany(mappedBy = "categories")
    @JsonBackReference("product-category")  // ← breaks Product↔Category loop
    private List<Product> products = new ArrayList<>();
}