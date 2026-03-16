package com.microservices.ordersystem.order_service.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter @Setter
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long customerId;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "order_id")
    private List<OrderItem> items;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;
    private LocalDateTime createdAt;

    @Setter(AccessLevel.NONE)
    private BigDecimal total;

    public Order() {};

    public Order(Long customerId, List<OrderItem> items) {
        this.customerId = customerId;
        this.items = items;
        this.status = OrderStatus.CREATED;
        this.createdAt = LocalDateTime.now();
        this.total = this.calculateTotal();
    }

    public BigDecimal calculateTotal() {
        return this.items.stream().map(OrderItem::getSubTotal).reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
