package com.microservices.ordersystem.product_service.dto;

import com.microservices.ordersystem.product_service.model.Category;
import lombok.Getter;

@Getter
public class ProductDTO {

    private Long id;
    private String name;
    private Category category;
    private Double price;

    public ProductDTO(Long id, String name, Category category, Double price) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.price = price;
    }
}
