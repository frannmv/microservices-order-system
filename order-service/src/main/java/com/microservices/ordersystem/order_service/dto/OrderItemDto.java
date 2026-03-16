package com.microservices.ordersystem.order_service.dto;

import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class OrderItemDto {

    private Long productId;
    private String productName;
    private Integer quantity;
    private BigDecimal unitPrice;

    public OrderItemDto() {};

    public OrderItemDto(Long productId, String productName, Integer quantity, BigDecimal unitPrice) {
        this.productId = productId;
        this.productName = productName;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
    }

    public BigDecimal getSubtotal() {
        return BigDecimal.valueOf(quantity).multiply(unitPrice);
    }
}
