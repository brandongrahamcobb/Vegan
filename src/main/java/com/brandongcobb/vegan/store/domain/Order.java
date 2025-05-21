package com.brandongcobb.vegan.store.domain;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "vegan_id", nullable = false)
    private Vegan vegan;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderLine> orderLines = new ArrayList<>();

    @Column(nullable = false)
    private Instant orderDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status;

    protected Order() {
    }

    public Order(Vegan vegan, List<OrderLine> orderLines) {
        this.vegan = vegan;
        this.orderLines = orderLines;
        this.orderDate = Instant.now();
        this.status = OrderStatus.PENDING;
        this.orderLines.forEach(line -> line.setOrder(this));
    }

    public Long getId() {
        return id;
    }

    public Vegan getVegan() {
        return vegan;
    }

    public void setVegan(Vegan vegan) {
        this.vegan = vegan;
    }

    public List<OrderLine> getOrderLines() {
        return orderLines;
    }

    public void setOrderLines(List<OrderLine> orderLines) {
        this.orderLines = orderLines;
        this.orderLines.forEach(line -> line.setOrder(this));
    }

    public Instant getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Instant orderDate) {
        this.orderDate = orderDate;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }
}