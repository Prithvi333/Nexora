package com.nexora.orders.order.model;

import com.nexora.orders.order.enums.OrderStatus;
import com.nexora.orders.orderItems.model.OrderItem;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "orders")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Orders {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Builder.Default
    private String uid = UUID.randomUUID().toString();

    @Column(nullable = false)
    private String userProfileUid;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    private OrderStatus status = OrderStatus.CREATED;

    @Column(nullable = false)
    private Double totalAmount;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> items;

    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();
    @Builder.Default
    @Column(updatable = false)
    private LocalDateTime updatedAt = LocalDateTime.now();
}

