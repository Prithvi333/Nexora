package com.nexora.payment.payment.service;

import com.nexora.common.events.PaymentStatusEvent;
import com.nexora.payment.exception.payment.PaymentException;
import com.nexora.payment.history.model.PaymentHistory;
import com.nexora.payment.history.repository.PaymentHistoryRepository;
import com.nexora.payment.kafka.enums.EventType;
import com.nexora.payment.kafka.producer.PaymentStatusEventProducer;
import com.nexora.payment.payment.enums.PaymentStatus;
import com.nexora.payment.payment.gateway.PaymentGateway;
import com.nexora.payment.payment.model.Payment;
import com.nexora.payment.payment.repository.PaymentRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class PaymentWebhookService {

    @Autowired
    private PaymentStatusEventProducer paymentStatusEventProducer;

    private final PaymentGateway paymentGateway;
    private final PaymentRepository paymentRepository;
    private final PaymentHistoryRepository paymentHistoryRepository;

    public void processWebhook(String payload, String signature) {

        boolean valid =
                paymentGateway.verifyWebhook(payload, signature);

        if (!valid) {
            throw new PaymentException("Invalid webhook signature");
        }

        JSONObject root = new JSONObject(payload);

        String eventType = root.getString("event");

        JSONObject paymentEntity = root
                .getJSONObject("payload")
                .getJSONObject("payment")
                .getJSONObject("entity");

        String gatewayOrderId =
                paymentEntity.getString("order_id");

        String gatewayPaymentId =
                paymentEntity.getString("id");

        Payment payment =
                paymentRepository
                        .findByGatewayOrderId(gatewayOrderId)
                        .orElseThrow(() ->
                                new PaymentException(
                                        "Payment not found"));

        PaymentStatus oldStatus =
                payment.getStatus();

        switch (eventType) {

            case "payment.authorized" -> {
                payment.setStatus(PaymentStatus.AUTHORIZED);
            }

            case "payment.captured" -> {

                payment.setStatus(PaymentStatus.SUCCESS);
                paymentStatusEventProducer.publishOrderStatusEvent(PaymentStatusEvent.builder()
                        .eventType(EventType.PAYMENT_SUCCESS)
                        .userUid(payment.getUserUid())
                        .orderUid(payment.getOrderUid())
                        .orderStatus("CONFIRMED")
                        .paymentStatus(PaymentStatus.SUCCESS.name())
                        .build());
                payment.setGatewayPaymentId(
                        gatewayPaymentId);

                payment.setFailureReason(null);
            }

            case "payment.failed" -> {

                payment.setStatus(PaymentStatus.FAILED);
                paymentStatusEventProducer.publishOrderStatusEvent(PaymentStatusEvent.builder()
                        .eventType(EventType.PAYMENT_FAILED)
                        .userUid(payment.getUserUid())
                        .orderUid(payment.getOrderUid())
                        .orderStatus("CREATED")
                        .paymentStatus(PaymentStatus.FAILED.name())
                        .build());
                payment.setGatewayPaymentId(
                        gatewayPaymentId);

                payment.setRetryCount(
                        payment.getRetryCount() + 1);

                payment.setFailureReason(
                        paymentEntity.optString(
                                "error_description",
                                "Payment failed"));
            }

            default -> {
                return;
            }
        }

        paymentRepository.save(payment);

        savePaymentHistory(
                payment,
                oldStatus,
                payment.getStatus(),
                eventType,
                payload
        );
    }

    private void savePaymentHistory(
            Payment payment,
            PaymentStatus fromStatus,
            PaymentStatus toStatus,
            String eventType,
            String payload) {

        PaymentHistory history =
                PaymentHistory.builder()
                        .paymentId(
                                payment.getPaymentId())
                        .userUid(
                                payment.getUserUid())
                        .fromStatus(fromStatus)
                        .toStatus(toStatus)
                        .eventType(eventType)
                        .description(
                                "Payment status changed from "
                                        + fromStatus
                                        + " to "
                                        + toStatus)
                        .gatewayResponse(payload)
                        .triggeredBy("RAZORPAY_WEBHOOK")
                        .build();

        paymentHistoryRepository.save(history);
    }
}
