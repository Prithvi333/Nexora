package com.nexora.orders.kafka.consumer;

import com.nexora.common.events.BaseEvent;
import com.nexora.common.events.PaymentStatusEvent;
import com.nexora.orders.order.service.OrderService;
import com.nexora.orders.utility.constants.ITopics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class PaymentStatusConsumer {

    @Autowired
    private OrderService orderService;

    @KafkaListener(topics = ITopics.PAYMENT_PAYMENT_STATUS_EVENT)
    public void consumeOrderStatusEvent(BaseEvent baseEvent) {
        if (baseEvent instanceof PaymentStatusEvent paymentStatusEvent) {
            orderService.updateOrderStatus(paymentStatusEvent);
        }

    }
}
