package com.nexora.orders.orderItems.repository;

import com.nexora.orders.order.model.Orders;
import com.nexora.orders.orderItems.model.OrderItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    Optional<OrderItem> findByUid(String uid);

    Page<OrderItem> findByOrder(Orders order, Pageable pageable);


}
