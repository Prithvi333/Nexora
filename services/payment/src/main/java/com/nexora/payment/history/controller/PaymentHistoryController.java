package com.nexora.payment.history.controller;

import com.nexora.payment.history.service.PaymentHistoryService;
import com.nexora.payment.response.history.PaymentHistoryResponse;
import com.nexora.payment.utility.constants.IUrls;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(IUrls.USER + IUrls.HISTORY)
public class PaymentHistoryController {

    @Autowired
    private PaymentHistoryService paymentHistoryService;

    ResponseEntity<List<PaymentHistoryResponse>> getUserPaymentHistory(@RequestParam(required = false) Integer pageNo, @RequestParam(required = false) Integer pageSize, String sortBy, String direction) {
        return new ResponseEntity<>(paymentHistoryService.getPaymentHistory(pageNo, pageSize, sortBy, direction), HttpStatus.OK);
    }

}
