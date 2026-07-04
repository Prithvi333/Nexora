package com.nexora.orders.orderItems.repository;

import com.nexora.orders.order.model.Orders;
import com.nexora.orders.orderItems.model.OrderItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.expression.spel.ast.OpOr;

import java.util.Optional;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    Optional<OrderItem> findByUidAndOrder_UserProfileUid(String uid, String userProfileUid);

    Page<OrderItem> findByOrder(Orders order, Pageable pageable);


}
