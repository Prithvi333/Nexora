package com.nexora.payment.payment.service;

import com.nexora.payment.exception.payment.PaymentException;
import com.nexora.payment.payment.enums.CurrencyType;
import com.nexora.payment.payment.enums.PaymentMethod;
import com.nexora.payment.payment.gateway.PaymentGateway;
import com.nexora.payment.payment.model.Payment;
import com.nexora.payment.payment.repository.PaymentRepository;
import com.nexora.payment.request.payment.CreatePaymentRequest;
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
    public void makePayment(CreatePaymentRequest createPaymentRequest) {
        Payment.PaymentBuilder paymentBuilder = GlobalUtility.convertFromPaymentEventToPayment(createPaymentRequest);
        if (!CurrencyType.isValid(createPaymentRequest.currency())) {
            throw new PaymentException("Currency type must be valid");
        }
        paymentBuilder.currencyType(CurrencyType.valueOf(createPaymentRequest.currency()));
        if (!PaymentMethod.isValid(createPaymentRequest.paymentMethod())) {
            throw new PaymentException("Payment method should be valid");
        }
        paymentBuilder.paymentMethod(PaymentMethod.valueOf(createPaymentRequest.paymentMethod()));
        paymentBuilder.idempotencyKey(createPaymentRequest.userUid().substring(0, 6) + createPaymentRequest.orderUid().substring(0, 6));
        try {
            String paymentId = razorpayPaymentGateway.createOrder(createPaymentRequest.amount(), createPaymentRequest.currency(), createPaymentRequest.orderUid());
            paymentBuilder.paymentId(paymentId);
        } catch (Exception e) {
            throw new PaymentException();
        }

        GlobalUtility.convertFromPaymentToPaymentResponse(paymentRepository.save(paymentBuilder.build()));
    }
}
