package com.nexora.payment.history.service;

import com.nexora.payment.response.history.PaymentHistoryResponse;

import java.util.List;

public interface AdminPaymentHistoryService {

    List<PaymentHistoryResponse> getAllPaymentHistory(Integer pageNo, Integer pageSize, String sortBy, String direction);


}
