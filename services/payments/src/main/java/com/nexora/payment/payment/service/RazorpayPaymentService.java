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
    public void makePayment(CreatePaymentRequest request) {

        Payment.PaymentBuilder paymentBuilder =
                GlobalUtility.convertFromPaymentEventToPayment(request);

        if (!CurrencyType.isValid(request.currency())) {
            throw new PaymentException("Currency type must be valid");
        }

        paymentBuilder.currencyType(
                CurrencyType.valueOf(request.currency()));

        if (!PaymentMethod.isValid(request.paymentMethod())) {
            throw new PaymentException("Payment method should be valid");
        }

        paymentBuilder.paymentMethod(
                PaymentMethod.valueOf(request.paymentMethod()));

        paymentBuilder.idempotencyKey(
                request.userUid().substring(0, 6)
                        + request.orderUid().substring(0, 6));

        try {

            String razorpayOrderId =
                    razorpayPaymentGateway.createOrder(
                            request.amount(),
                            request.currency(),
                            request.orderUid());

            paymentBuilder.gatewayOrderId(razorpayOrderId);

        } catch (Exception e) {
            throw new PaymentException("Unable to create Razorpay order");
        }

        paymentRepository.save(paymentBuilder.build());
    }
}
