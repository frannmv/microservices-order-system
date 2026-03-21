package com.microservices.ordersystem.product_service.mapper;

import com.microservices.ordersystem.product_service.dto.ProductDto;
import com.microservices.ordersystem.product_service.model.Product;

public class Mapper {

    public static ProductDto toDto(Product p) {

        if(p == null) return null;

        return new ProductDto(
                p.getId(),
                p.getName(),
                p.getCategory(),
                p.getPrice(),
                p.getStatus().toString()
        );
    }
}
