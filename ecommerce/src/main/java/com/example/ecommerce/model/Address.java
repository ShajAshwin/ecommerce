package com.example.ecommerce.model;

import com.example.ecommerce.model.User;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String street;
    private String city;
    private String state;
    private String pinCode;
    private String country;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonBackReference("user-address")      // ← breaks User↔Address loop
    private User user;
}