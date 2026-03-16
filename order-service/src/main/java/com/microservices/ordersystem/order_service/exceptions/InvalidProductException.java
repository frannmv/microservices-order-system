package com.microservices.ordersystem.order_service.exceptions;

public class InvalidProductException extends RuntimeException {
    public InvalidProductException(String message) {
        super(message);
    }
}
