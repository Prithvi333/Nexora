package com.nexora.orders.order.repository;

import com.nexora.orders.order.model.Orders;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrderRepository extends JpaRepository<Orders, Long> {


    Optional<Orders> findByUidAndUserUid(String uid, String userUid);

    Page<Orders> findByUserUid(String userUid, Pageable pageable);
}
