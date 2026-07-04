package com.nexora.payment.payment.controller;

import com.nexora.payment.payment.service.PaymentService;
import com.nexora.payment.response.payment.PaymentResponse;
import com.nexora.payment.utility.constants.IUrls;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(IUrls.USER)
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @GetMapping(IUrls.PAYMENTS)
    public ResponseEntity<PaymentResponse> getPayments(@RequestParam String orderUid) {
        return new ResponseEntity<>(paymentService.getPaymentResponseByOrderUid(orderUid), HttpStatus.OK);
    }

}
