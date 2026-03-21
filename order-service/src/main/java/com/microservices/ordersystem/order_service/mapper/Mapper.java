package com.microservices.ordersystem.order_service.mapper;

import com.microservices.ordersystem.order_service.dto.response.OrderResponse;
import com.microservices.ordersystem.order_service.dto.response.OrderItemResponse;
import com.microservices.ordersystem.order_service.model.Order;
import com.microservices.ordersystem.order_service.model.OrderItem;

public class Mapper {

    public static OrderResponse toDto(Order order) {

        if(order == null) return null;

        return new OrderResponse(
                order.getId(),
                order.getCustomerId(),
                order.getItems().stream().map(Mapper::toDto).toList(),
                order.getTotal(),
                order.getStatus().toString(),
                order.getCreatedAt()
        );
    }

    public static OrderItemResponse toDto(OrderItem item) {

        if(item == null) return null;

        return new OrderItemResponse(
                item.getProductId(),
                item.getProductName(),
                item.getQuantity(),
                item.getUnitPrice()
        );
    }
}