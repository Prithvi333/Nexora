package com.nexora.payment.history.service;

import com.nexora.payment.response.history.PaymentHistoryResponse;
import com.nexora.payment.response.payment.PaymentResponse;

import java.util.List;

public interface PaymentHistoryService {

    List<PaymentHistoryResponse> getPaymentHistory(Integer pageNo, Integer pageSize, String sortBy, String direction);

}
