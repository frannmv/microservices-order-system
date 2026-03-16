package com.microservices.ordersystem.order_service.model;

public enum OrderStatus {
    CREATED,
    PENDING_PAYMENT,
    PAID,
    CANCELLED,
    FAILED,
    COMPLETED
}
