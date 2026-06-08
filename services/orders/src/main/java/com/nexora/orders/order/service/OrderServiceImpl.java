package com.nexora.orders.order.service;

import com.nexora.orders.clients.feign.products.ProductClient;
import com.nexora.orders.clients.feign.users.UserClient;
import com.nexora.orders.exception.order.OrderNotFound;
import com.nexora.orders.order.model.Orders;
import com.nexora.orders.order.repository.OrderRepository;
import com.nexora.orders.orderItems.model.OrderItem;
import com.nexora.orders.request.order.CreateOrderRequest;
import com.nexora.orders.request.orderItems.OrderItemRequest;
import com.nexora.orders.response.order.OrderResponse;
import com.nexora.orders.response.order.VariantPriceResponse;
import com.nexora.orders.utility.GlobalUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserClient userClient;

    @Autowired
    private ProductClient productClient;

    @Override
    public OrderResponse createOrder(CreateOrderRequest orderRequest) {
        userClient.isUserExists(orderRequest.userUid());
        Orders.OrdersBuilder order = Orders.builder().userUid(orderRequest.userUid());
        List<VariantPriceResponse> productResponse = productClient.getProducts(orderRequest.items());
        Map<String, Double> variantPriceResponse = productResponse.stream().collect(Collectors.toMap(VariantPriceResponse::variantUid, VariantPriceResponse::price));
        List<OrderItem> orderItems = createOrderItems(variantPriceResponse, orderRequest.items());

        Double totalAmount = orderItems.stream()
                .mapToDouble(OrderItem::getPrice)
                .sum();
        order.totalAmount(totalAmount);
        order.items(orderItems);

        return GlobalUtility.convertFromOrderToOrderResponse(orderRepository.save(order.build()));
    }


    private List<OrderItem> createOrderItems(Map<String, Double> variantPriceResponse, List<OrderItemRequest> orderItemRequests) {
        return orderItemRequests.stream().map(orderItem ->
                OrderItem.builder().productUid(orderItem.productUid()).quantity(orderItem.quantity())
                        .variantUid(orderItem.variantUid())
                        .price(variantPriceResponse.get(orderItem.variantUid())).build()).toList();
    }

    @Override
    public OrderResponse fetchOrder(String orderUid) {
        Orders order = orderRepository.findByUid(orderUid).orElseThrow(() -> new OrderNotFound(orderUid));
        return GlobalUtility.convertFromOrderToOrderResponse(order);
    }
}
