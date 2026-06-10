package com.nexora.orders.order.service;

import com.nexora.orders.clients.feign.products.ProductClient;
import com.nexora.orders.clients.feign.users.UserClient;
import com.nexora.orders.exception.order.EmptyOrderList;
import com.nexora.orders.exception.order.OrderNotFound;
import com.nexora.orders.history.model.OrderHistory;
import com.nexora.orders.history.service.OrderHistoryService;
import com.nexora.orders.order.enums.OrderStatus;
import com.nexora.orders.order.model.Orders;
import com.nexora.orders.order.repository.OrderRepository;
import com.nexora.orders.orderItems.model.OrderItem;
import com.nexora.orders.request.order.CreateOrderRequest;
import com.nexora.orders.request.orderItems.OrderItemRequest;
import com.nexora.orders.response.order.OrderResponse;
import com.nexora.orders.response.order.VariantPriceResponse;
import com.nexora.orders.utility.GlobalUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderHistoryService orderHistoryService;

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
        Orders savedOrder = orderRepository.save(order.build());
        OrderHistory orderHistory = GlobalUtility.convertFromArgsToOrderHistory(savedOrder.getUid());
        orderHistoryService.createOrderHistory(orderHistory);
        return GlobalUtility.convertFromOrderToOrderResponse(savedOrder);
    }


    private List<OrderItem> createOrderItems(Map<String, Double> variantPriceResponse, List<OrderItemRequest> orderItemRequests) {
        return orderItemRequests.stream().map(orderItem ->
                OrderItem.builder().productUid(orderItem.productUid()).quantity(orderItem.quantity())
                        .variantUid(orderItem.variantUid())
                        .price(variantPriceResponse.get(orderItem.variantUid())).build()).toList();
    }

    @Override
    public List<OrderResponse> fetchOrder(String orderUid, Integer pageNo, Integer pageSize, String sortBy, String direction) {
        String userUid = GlobalUtility.getLoggedInUserDetails().userUid();
        Optional<Orders> order = orderRepository.findByUidAndUserUid(orderUid, userUid);
        if (order.isPresent()) {
            return List.of(GlobalUtility.convertFromOrderToOrderResponse(order.get()));
        }

        Pageable pageable = GlobalUtility.getPageable(pageNo, pageSize, sortBy, direction);

        Page<Orders> ordersPage = orderRepository.findByUserUid(userUid, pageable);

        if (ordersPage.isEmpty()) {
            throw new EmptyOrderList();
        }

        return ordersPage.getContent().stream().map(GlobalUtility::convertFromOrderToOrderResponse).toList();


    }
}
