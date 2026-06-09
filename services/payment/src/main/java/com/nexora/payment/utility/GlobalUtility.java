package com.nexora.payment.utility;

import com.nexora.payment.history.model.PaymentHistory;
import com.nexora.payment.kafka.event.CreatePaymentEvent;
import com.nexora.payment.payment.model.Payment;
import com.nexora.payment.response.history.PaymentHistoryResponse;
import com.nexora.payment.response.payment.PaymentResponse;
import com.nexora.payment.security.UserPrinciple;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;

public class GlobalUtility {

    public static Pageable getPageable(Integer pageNo, Integer pageSize, String sortBy, String direction) {
        pageSize = pageSize == null ? 5 : pageSize;
        pageNo = pageNo == null ? 0 : pageNo;
        Sort sort = direction == null ? Sort.by(sortBy).ascending() : direction.equals("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        return PageRequest.of(pageNo, pageSize, sort);
    }

    public static Payment.PaymentBuilder convertFromPaymentEventToPayment(CreatePaymentEvent paymentEvent) {
        return Payment.builder().orderUid(paymentEvent.orderUid())
                .userUid(paymentEvent.userUid())
                .amount(paymentEvent.amount())
                .paymentMethod(paymentEvent.paymentMethod())
                .idempotencyKey(paymentEvent.idempotencyKey());
    }

    public static PaymentResponse convertFromPaymentToPaymentResponse(Payment payment) {
        return PaymentResponse.builder()
                .paymentId(payment.getPaymentId())
                .gatewayPaymentId(payment.getGatewayPaymentId())
                .gatewayName(payment.getGatewayName())
                .createdAt(payment.getCreatedAt())
                .currency(payment.getCurrencyType().name())
                .updatedAt(payment.getUpdatedAt())
                .status(payment.getStatus().name())
                .paymentMethod(payment.getPaymentMethod().name())
                .userUid(payment.getUserUid())
                .orderUid(payment.getOrderUid())
                .amount(payment.getAmount())
                .build();
    }

    public static PaymentHistoryResponse convertFromPaymentHistoryToPaymentHistoryResponse(PaymentHistory paymentHistory) {
        return PaymentHistoryResponse.builder()
                .paymentUid(paymentHistory.getPaymentId())
                .fromStatus(paymentHistory.getFromStatus().name())
                .toStatus(paymentHistory.getToStatus().name())
                .eventType(paymentHistory.getEventType())
                .triggeredBy(paymentHistory.getTriggeredBy())
                .description(paymentHistory.getDescription())
                .createdAt(paymentHistory.getCreatedAt())
                .build();
    }


    public static UserPrinciple getLoggedInUserDetails() {
        return (UserPrinciple) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

}
