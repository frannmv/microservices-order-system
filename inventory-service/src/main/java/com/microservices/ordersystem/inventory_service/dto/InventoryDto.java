package com.microservices.ordersystem.inventory_service.dto;

import lombok.Getter;

@Getter
public class InventoryDto {

    private Long productId;
    private Integer stock;

    protected InventoryDto() {};

    public InventoryDto(Long productId, Integer stock) {
        this.productId = productId;
        this.stock = stock;
    }
}
