package com.nexora.payment.payment;

import com.nexora.payment.payment.service.PaymentWebhookService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@Slf4j
@RequestMapping("/api/v1/payments")
public class PaymentWebhookController {

    @Autowired
    private PaymentWebhookService paymentWebhookService;

    @PostMapping("/webhook")
    public ResponseEntity<Void> handleWebhook(
            @RequestBody String payload,
            @RequestHeader("X-Razorpay-Signature")
            String signature) {
        log.info("Received Razorpay webhook");
        paymentWebhookService
                .processWebhook(payload, signature);
        log.info("Webhook processed successfully");
        return ResponseEntity.ok().build();
    }
}
