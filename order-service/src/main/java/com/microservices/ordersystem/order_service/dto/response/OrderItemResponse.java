package com.microservices.ordersystem.order_service.dto.response;

import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class OrderItemResponse {

    private Long productId;
    private String productName;
    private Integer quantity;
    private BigDecimal unitPrice;

    public OrderItemResponse() {};

    public OrderItemResponse(Long productId, String productName, Integer quantity, BigDecimal unitPrice) {
        this.productId = productId;
        this.productName = productName;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
    }

    public BigDecimal getSubtotal() {
        return BigDecimal.valueOf(quantity).multiply(unitPrice);
    }
}
