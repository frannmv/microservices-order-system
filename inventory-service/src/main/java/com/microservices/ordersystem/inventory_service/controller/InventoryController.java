package com.microservices.ordersystem.inventory_service.controller;

import com.microservices.ordersystem.inventory_service.dto.InventoryDto;
import com.microservices.ordersystem.inventory_service.mapper.Mapper;
import com.microservices.ordersystem.inventory_service.model.Inventory;
import com.microservices.ordersystem.inventory_service.service.InventoryService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/inventory")
public class InventoryController {

    private final InventoryService service;

    public InventoryController(InventoryService service) {
        this.service = service;
    }

    @GetMapping
    public List<InventoryDto> getInventory() {
        return this.service.getInventory().stream().map(Mapper::toDto).toList();
    }

    @GetMapping("/{productId}")
    public InventoryDto getProductInventory(@PathVariable Long productId) {
        return Mapper.toDto(this.service.getProductInventory(productId));
    }

    @PostMapping
    public InventoryDto newInventory(@RequestBody Inventory inventory) {
        return Mapper.toDto(this.service.newInventory(inventory));
    }

    @PatchMapping("/{productId}/increase")
    public InventoryDto increaseStock(@PathVariable Long productId,
                                @RequestParam Integer quantity) {
        return Mapper.toDto(this.service.increaseStock(productId, quantity));
    }

    @PatchMapping("/{productId}/decrease")
    public InventoryDto decreaseStock(@PathVariable Long productId,
                                @RequestParam Integer quantity) {
        return Mapper.toDto(this.service.decreaseStock(productId, quantity));
    }
}
