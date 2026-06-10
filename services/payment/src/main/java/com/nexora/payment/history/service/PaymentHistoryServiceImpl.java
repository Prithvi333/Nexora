package com.nexora.payment.history.service;

import com.nexora.payment.exception.history.EmptyPaymentHistoryList;
import com.nexora.payment.history.model.PaymentHistory;
import com.nexora.payment.history.repository.PaymentHistoryRepository;
import com.nexora.payment.response.history.PaymentHistoryResponse;
import com.nexora.payment.utility.GlobalUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PaymentHistoryServiceImpl implements PaymentHistoryService {

    @Autowired
    private PaymentHistoryRepository paymentHistoryRepository;

    @Override
    public List<PaymentHistoryResponse> getPaymentHistory(Integer pageNo, Integer pageSize, String sortBy, String direction) {
        String userUid = GlobalUtility.getLoggedInUserDetails().userUid();
        sortBy = sortBy == null ? "paymentId" : sortBy;
        Pageable pageable = GlobalUtility.getPageable(pageNo, pageSize, sortBy, direction);
        Page<PaymentHistory> paymentHistories = paymentHistoryRepository.findByUserUid(userUid, pageable);
        if (paymentHistories.isEmpty()) {
            throw new EmptyPaymentHistoryList();
        }
        return paymentHistories.getContent().stream().map(GlobalUtility::convertFromPaymentHistoryToPaymentHistoryResponse).toList();

    }
}
