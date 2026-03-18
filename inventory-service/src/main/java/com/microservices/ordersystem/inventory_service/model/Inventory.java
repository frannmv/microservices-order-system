package com.microservices.ordersystem.inventory_service.model;

import com.microservices.ordersystem.inventory_service.exceptions.InsufficientStockException;
import com.microservices.ordersystem.inventory_service.exceptions.InvalidInventoryException;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Table(name = "stock")
@Getter
public class Inventory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private Long productId;
    private Integer stock;

    protected Inventory() {}

    public Inventory(Long productId, Integer stock) {

        if(productId == null || productId <= 0) throw new InvalidInventoryException("The product id must be positive");
        if(stock == null || stock < 0) throw new InvalidInventoryException("Stock must be zero or positive");

        this.productId = productId;
        this.stock = stock;
    }

    public void decreaseStock(Integer quantity) {
        if(quantity <= 0) throw new InvalidInventoryException("Quantity must be positive");
        if(this.stock - quantity < 0) throw new InsufficientStockException("Not enough stock for product: " + this.productId);
        this.stock -= quantity;
    }

    public void addStock(Integer quantity) {
        if(quantity <= 0) throw new InvalidInventoryException("Quantity must be positive");
        this.stock += quantity;
    }
}
