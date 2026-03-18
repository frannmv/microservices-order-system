package com.microservices.ordersystem.order_service.model;

import com.microservices.ordersystem.order_service.exceptions.InvalidOrderException;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.springframework.util.Assert;

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

        if(items == null || items.isEmpty()) throw new InvalidOrderException("The order must have at least one item");
        if(customerId == null || customerId <= 0) throw new InvalidOrderException("The customer id must be positive");

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
