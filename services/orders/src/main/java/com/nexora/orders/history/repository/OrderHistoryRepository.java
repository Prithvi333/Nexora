package com.nexora.orders.history.repository;

import com.nexora.orders.history.model.OrderHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderHistoryRepository extends JpaRepository<OrderHistory, Long> {
    Page<OrderHistory> findByOrderUid(String orderUid, Pageable pageable);

    Page<OrderHistory> findByUserProfileUid(String userUid, Pageable pageable);

}
