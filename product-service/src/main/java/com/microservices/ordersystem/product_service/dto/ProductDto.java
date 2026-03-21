package com.microservices.ordersystem.product_service.dto;

import com.microservices.ordersystem.product_service.model.Category;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class ProductDto {

    private Long id;
    private String name;
    private Category category;
    private BigDecimal price;
    private String status;

    public ProductDto(Long id, String name, Category category, BigDecimal price, String status) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.price = price;
        this.status = status;
    }
}
