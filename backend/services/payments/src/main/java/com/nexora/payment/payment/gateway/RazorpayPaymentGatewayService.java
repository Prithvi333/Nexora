package com.nexora.payment.payment.gateway;

import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.razorpay.Utils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class RazorpayPaymentGatewayService implements PaymentGateway {

    @Autowired
    private RazorpayClient razorpayClient;

    @Value("${razorpay.key-secret}")
    private String secret;

    @Override
    public String createOrder(Double amount, String currency, String receiptId) throws Exception {

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("amount", amount * 100);
        jsonObject.put("currency", currency);
        jsonObject.put("receipt", receiptId);

        Order order = razorpayClient.orders.create(jsonObject);
        return order.get("id");
    }

    @Override
    public boolean verifyWebhook(String payload, String signature) {
        try {
            return Utils.verifyWebhookSignature(payload, signature, secret);
        } catch (RazorpayException e) {
            throw new RuntimeException(e);
        }
    }
}
