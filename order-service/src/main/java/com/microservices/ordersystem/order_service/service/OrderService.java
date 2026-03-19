package com.microservices.ordersystem.order_service.service;

import com.microservices.ordersystem.order_service.client.InventoryRestClient;
import com.microservices.ordersystem.order_service.client.ProductRestClient;
import com.microservices.ordersystem.order_service.client.UserRestClient;
import com.microservices.ordersystem.order_service.exceptions.*;

import com.microservices.ordersystem.order_service.model.Order;
import com.microservices.ordersystem.order_service.model.OrderItem;
import com.microservices.ordersystem.order_service.repository.OrderRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class OrderService {

    private final OrderRepository repo;
    private final UserRestClient userClient;
    private final ProductRestClient productClient;
    private final InventoryRestClient inventoryClient;

    public List<Order> getOrders() {
        return this.repo.findAll();
    }

    public Order getOrderById(Long id) {
        return this.repo.findById(id).orElseThrow(() -> new OrderNotFoundException("Order with id: " + id + " was not found"));
    }

    @Transactional
    public Order create(Order order) {

        this.userClient.isValid(order.getCustomerId());

        for(OrderItem item : order.getItems()) {

            if(item.getQuantity() <= 0) throw new InvalidQuantityException("Quantity must be positive");

            this.productClient.isValid(item.getProductId());
            this.inventoryClient.updateStock(item.getProductId(), item.getQuantity());
        }
        return this.repo.save(order);
    }

    public void delete(Long id) {
        this.repo.delete(this.getOrderById(id));
    }
}
