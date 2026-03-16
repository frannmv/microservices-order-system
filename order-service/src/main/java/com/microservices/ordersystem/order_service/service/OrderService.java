package com.microservices.ordersystem.order_service.service;

import com.microservices.ordersystem.order_service.client.ProductRestClient;
import com.microservices.ordersystem.order_service.client.UserRestClient;
import com.microservices.ordersystem.order_service.dto.ProductDto;
import com.microservices.ordersystem.order_service.dto.UserDto;
import com.microservices.ordersystem.order_service.exceptions.CustomerNotFoundException;
import com.microservices.ordersystem.order_service.exceptions.InvalidProductException;
import com.microservices.ordersystem.order_service.exceptions.OrderNotFoundException;
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

    public List<Order> getOrders() {
        return this.repo.findAll();
    }

    public Order getOrderById(Long id) {
        return this.repo.findById(id).orElseThrow(() -> new OrderNotFoundException("Order with id: " + id + " was not found"));
    }

    @Transactional
    public Order create(Order order) {

        UserDto customer = this.userClient.findById(order.getCustomerId());
        if(customer == null) throw new CustomerNotFoundException("The customer with id " + order.getCustomerId() + " was not found");

        for(OrderItem item : order.getItems()) {

            ProductDto product = this.productClient.findById(item.getProductId());

            if(product == null) {
                throw new InvalidProductException("The product with id " + item.getProductId() + " was not found");
            }

            if(product.getStatus().equals("INACTIVE")) {
                throw new InvalidProductException("The product with id " + item.getProductId() + " is not available");
            }
        }

        return this.repo.save(order);
    }

    public void delete(Long id) {
        this.repo.delete(this.getOrderById(id));
    }
}
