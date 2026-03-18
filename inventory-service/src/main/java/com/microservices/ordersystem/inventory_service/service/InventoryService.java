package com.microservices.ordersystem.inventory_service.service;

import com.microservices.ordersystem.inventory_service.exceptions.InventoryNotFoundException;
import com.microservices.ordersystem.inventory_service.model.Inventory;
import com.microservices.ordersystem.inventory_service.repository.InventoryRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class InventoryService {

    private final InventoryRepository repo;

    public List<Inventory> getInventory() {
        return this.repo.findAll();
    }

    public Inventory getProductInventory(Long productId) {
        return this.repo.findByProductId(productId).orElseThrow(() -> new InventoryNotFoundException("No inventory found for product " + productId));
    }

    public Inventory newInventory(Inventory inventory) {
        return this.repo.save(inventory);
    }

    @Transactional
    public Inventory increaseStock(Long productId, Integer quantity) {
        Inventory inventory = this.getProductInventory(productId);
        inventory.addStock(quantity);
        return this.repo.save(inventory);
    }

    @Transactional
    public Inventory decreaseStock(Long productId, Integer quantity) {
        Inventory inventory = this.getProductInventory(productId);
        inventory.decreaseStock(quantity);
        return this.repo.save(inventory);
    }
}
