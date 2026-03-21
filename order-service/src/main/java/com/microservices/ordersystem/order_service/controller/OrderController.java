package com.microservices.ordersystem.order_service.controller;

import com.microservices.ordersystem.order_service.dto.response.OrderResponse;
import com.microservices.ordersystem.order_service.dto.request.CreateOrderRequest;
import com.microservices.ordersystem.order_service.mapper.Mapper;
import com.microservices.ordersystem.order_service.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService service;

    public OrderController(OrderService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<OrderResponse>> getOrders() {
        return ResponseEntity.ok(this.service.getOrders().stream().map(Mapper::toDto).toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> getOrderById(@PathVariable Long id) {
        return ResponseEntity.ok(Mapper.toDto(this.service.getOrderById(id)));
    }

    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(@RequestBody CreateOrderRequest order) {
        OrderResponse created = Mapper.toDto(this.service.create(order));
        return ResponseEntity.created(URI.create("/orders/" + created.getId())).body(created);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
        this.service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
