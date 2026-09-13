package com.nexora.notification.kafka.consumer;

import com.nexora.common.events.BaseEvent;
import com.nexora.common.events.PaymentStatusEvent;
import com.nexora.notification.notification.service.NotificationService;
import com.nexora.notification.request.notification.NotificationRequest;
import com.nexora.notification.utils.constants.ITopics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class PaymentStatusEventConsumer {

    @Autowired
    private NotificationService notificationService;

    @KafkaListener(topics = ITopics.PAYMENT_PAYMENT_STATUS_EVENT)
    public void consumerPaymentStatusEvent(BaseEvent baseEvent) {
        if (baseEvent instanceof PaymentStatusEvent paymentStatusEvent) {

            String message = switch (paymentStatusEvent.getEventType()) {

                case PAYMENT_SUCCESS -> "Your payment for order "
                        + paymentStatusEvent.getOrderUid()
                        + " was successful.";

                case PAYMENT_FAILED -> "Your payment for order "
                        + paymentStatusEvent.getOrderUid()
                        + " has failed.";


                default -> "Payment status updated for order "
                        + paymentStatusEvent.getOrderUid();
            };

            notificationService.sendNotification(NotificationRequest.builder()
                    .eventType(paymentStatusEvent.getEventType())
                    .email(baseEvent.getEmail())
                    .userUid(paymentStatusEvent.getUserUid())
                    .message(message)
                    .build());
        }
    }


}
