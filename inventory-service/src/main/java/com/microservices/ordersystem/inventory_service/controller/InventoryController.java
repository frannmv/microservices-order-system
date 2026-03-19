package com.microservices.ordersystem.inventory_service.controller;

import com.microservices.ordersystem.inventory_service.dto.InventoryDto;
import com.microservices.ordersystem.inventory_service.mapper.Mapper;
import com.microservices.ordersystem.inventory_service.model.Inventory;
import com.microservices.ordersystem.inventory_service.service.InventoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/inventory")
public class InventoryController {

    private final InventoryService service;

    public InventoryController(InventoryService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<InventoryDto>> getInventory() {
        return ResponseEntity.ok(this.service.getInventory().stream().map(Mapper::toDto).toList());
    }

    @GetMapping("/{productId}")
    public ResponseEntity<InventoryDto> getProductInventory(@PathVariable Long productId) {
        return ResponseEntity.ok(Mapper.toDto(this.service.getProductInventory(productId)));
    }

    @PostMapping
    public ResponseEntity<InventoryDto> newInventory(@RequestBody Inventory inventory) {
        InventoryDto created = Mapper.toDto(this.service.newInventory(inventory));
        return ResponseEntity.created(URI.create("/inventory/"+ created.getProductId())).body(created);
    }

    @PatchMapping("/{productId}/increase")
    public ResponseEntity<InventoryDto> increaseStock(@PathVariable Long productId,
                                @RequestParam Integer quantity) {
        return ResponseEntity.ok(Mapper.toDto(this.service.increaseStock(productId, quantity)));
    }

    @PatchMapping("/{productId}/decrease")
    public ResponseEntity<InventoryDto> decreaseStock(@PathVariable Long productId,
                                @RequestParam Integer quantity) {
        return ResponseEntity.ok(Mapper.toDto(this.service.decreaseStock(productId, quantity)));
    }
}
