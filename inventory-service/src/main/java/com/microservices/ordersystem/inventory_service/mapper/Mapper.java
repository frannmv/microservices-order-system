package com.microservices.ordersystem.inventory_service.mapper;

import com.microservices.ordersystem.inventory_service.dto.InventoryDto;
import com.microservices.ordersystem.inventory_service.model.Inventory;

public class Mapper {

    public static InventoryDto toDto(Inventory inventory) {

        if(inventory == null) return null;

        return new InventoryDto(
                inventory.getProductId(),
                inventory.getStock()
        );
    }
}
