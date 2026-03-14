package com.microservices.ordersystem.product_service.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @Enumerated(EnumType.STRING)
    private Category category;
    private Double price;

    @Enumerated(EnumType.STRING)
    private Status status;

    public Product() {}

    public Product(String name, Category category, Double price, Status status) {
        this.name = name;
        this.category = category;
        this.price = price;
        this.status = status;
    }
}
