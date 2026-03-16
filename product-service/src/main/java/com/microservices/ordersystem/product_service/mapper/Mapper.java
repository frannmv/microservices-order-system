package com.microservices.ordersystem.product_service.mapper;

import com.microservices.ordersystem.product_service.dto.ProductDTO;
import com.microservices.ordersystem.product_service.model.Product;

public class Mapper {

    public static ProductDTO toDto(Product p) {

        if(p == null) return null;

        return new ProductDTO(
                p.getId(),
                p.getName(),
                p.getCategory(),
                p.getPrice(),
                p.getStatus().toString()
        );
    }
}
