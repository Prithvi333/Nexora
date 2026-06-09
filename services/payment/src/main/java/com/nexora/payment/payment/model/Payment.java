package com.nexora.payment.payment.model;

import com.nexora.payment.payment.enums.CurrencyType;
import com.nexora.payment.payment.enums.PaymentMethod;
import com.nexora.payment.payment.enums.PaymentStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "payments",
        indexes = {
                @Index(name = "idx_payment_id", columnList = "paymentId"),
                @Index(name = "idx_order_id", columnList = "orderUid"),
                @Index(name = "idx_user_id", columnList = "userUid")
        })
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, updatable = false)
    private String paymentId;

    @Column(nullable = false)
    private String orderUid;

    @Column(nullable = false)
    private String userUid;

    @Column(nullable = false)
    @DecimalMin(value = "1.0", message = "Amount must be greater than 0")
    private Double amount;

    @Column(nullable = false)
    @Builder.Default
    private CurrencyType currencyType = CurrencyType.INR;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private PaymentStatus status = PaymentStatus.CREATED;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentMethod paymentMethod;

    @Column(nullable = false)
    private String gatewayName;

    private String gatewayPaymentId;

    @Column(unique = true)
    private String idempotencyKey;

    @Min(value = 0)
    private Integer retryCount = 0;

    private String failureReason;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}