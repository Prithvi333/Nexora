package com.nexora.orders.order.repository;

import com.nexora.orders.order.model.Orders;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrderRepository extends JpaRepository<Orders, Long> {

    Optional<Orders> findByUid(String uid);
}
