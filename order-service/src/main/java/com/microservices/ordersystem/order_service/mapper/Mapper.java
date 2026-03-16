package com.microservices.ordersystem.order_service.mapper;

import com.microservices.ordersystem.order_service.dto.OrderDto;
import com.microservices.ordersystem.order_service.dto.OrderItemDto;
import com.microservices.ordersystem.order_service.model.Order;
import com.microservices.ordersystem.order_service.model.OrderItem;

public class Mapper {

    public static OrderDto toDto(Order order) {

        if(order == null) return null;

        return new OrderDto(
                order.getId(),
                order.getCustomerId(),
                order.getItems().stream().map(Mapper::toDto).toList(),
                order.getTotal(),
                order.getStatus().toString(),
                order.getCreatedAt()
        );
    }

    public static OrderItemDto toDto(OrderItem item) {

        if(item == null) return null;

        return new OrderItemDto(
                item.getProductId(),
                item.getProductName(),
                item.getQuantity(),
                item.getUnitPrice()
        );
    }
}