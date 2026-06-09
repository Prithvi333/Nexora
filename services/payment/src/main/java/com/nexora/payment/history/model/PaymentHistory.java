package com.nexora.payment.history.model;

import com.nexora.payment.payment.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "payment_history")
@Getter
@Setter
@AllArgsConstructor
@Builder
public class PaymentHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String paymentId;

    @Column(nullable = false)
    private String userId;

    @Enumerated(EnumType.STRING)
    private PaymentStatus fromStatus;

    @Enumerated(EnumType.STRING)
    private PaymentStatus toStatus;

    @Column(nullable = false)
    private String eventType;
    // INITIATED, WEBHOOK_RECEIVED, RETRY, REFUND

    @Column(length = 2000)
    private String description;

    @Column(columnDefinition = "TEXT")
    private String gatewayResponse;

    @Column(nullable = false)
    private String triggeredBy; // SYSTEM / USER / WEBHOOK

    @CreationTimestamp
    private LocalDateTime createdAt;
}