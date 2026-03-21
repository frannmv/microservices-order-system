package com.microservices.ordersystem.order_service.dto.external;

import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class ProductDto {

    private Long id;
    private String name;
    private BigDecimal price;
    private String status;

    public ProductDto() {};

    public ProductDto(Long id, String name,  BigDecimal price, String status) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.status = status;
    }
}
