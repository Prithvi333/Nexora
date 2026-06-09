package com.nexora.payment.payment.service;

import com.nexora.payment.exception.payment.PaymentException;
import com.nexora.payment.kafka.event.CreatePaymentEvent;
import com.nexora.payment.payment.gateway.PaymentGateway;
import com.nexora.payment.payment.model.Payment;
import com.nexora.payment.payment.repository.PaymentRepository;
import com.nexora.payment.response.payment.PaymentResponse;
import com.nexora.payment.utility.GlobalUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class RazorpayPaymentService implements PaymentService {

    @Autowired
    @Qualifier("razorpayPaymentGatewayService")
    private PaymentGateway razorpayPaymentGateway;

    @Autowired
    private PaymentRepository paymentRepository;

    @Override
    public PaymentResponse makePayment(CreatePaymentEvent paymentEvent) {
        Payment.PaymentBuilder paymentBuilder = GlobalUtility.convertFromPaymentEventToPayment(paymentEvent);
        try {
            String paymentId = razorpayPaymentGateway.createOrder(paymentEvent.amount(), paymentEvent.currencyType().name(), paymentEvent.orderUid());
            paymentBuilder.paymentId(paymentId);
        } catch (Exception e) {
            throw new PaymentException();
        }

        return GlobalUtility.convertFromPaymentToPaymentResponse(paymentRepository.save(paymentBuilder.build()));
    }
}
