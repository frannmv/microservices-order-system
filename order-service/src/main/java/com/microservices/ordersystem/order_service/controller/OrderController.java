package com.microservices.ordersystem.order_service.controller;

import com.microservices.ordersystem.order_service.dto.OrderDto;
import com.microservices.ordersystem.order_service.mapper.Mapper;
import com.microservices.ordersystem.order_service.model.Order;
import com.microservices.ordersystem.order_service.service.OrderService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService service;

    public OrderController(OrderService service) {
        this.service = service;
    }

    @GetMapping
    public List<OrderDto> getOrders() {
        return this.service.getOrders().stream().map(Mapper::toDto).toList();
    }

    @GetMapping("/{id}")
    public OrderDto getOrderById(@PathVariable Long id) {
        return Mapper.toDto(this.service.getOrderById(id));
    }

    @PostMapping
    public OrderDto createOrder(@RequestBody Order order) {
        return Mapper.toDto(this.service.create(order));
    }

    @DeleteMapping("/{id}")
    public void deleteOrder(@PathVariable Long id) {
        this.service.delete(id);
    }
}
