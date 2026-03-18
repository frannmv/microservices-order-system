package com.microservices.ordersystem.inventory_service.exceptions;

public class InvalidInventoryException extends RuntimeException {
    public InvalidInventoryException(String message) {
        super(message);
    }
}
