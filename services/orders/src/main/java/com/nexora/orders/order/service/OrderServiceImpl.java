package com.nexora.orders.order.service;

import com.nexora.common.events.OrderCreatedEvent;
import com.nexora.common.events.PaymentStatusEvent;
import com.nexora.common.events.PaymentRequestEvent;
import com.nexora.orders.config.feign.products.ProductClient;
import com.nexora.orders.config.feign.users.UserClient;
import com.nexora.orders.exception.order.EmptyOrderList;
import com.nexora.orders.exception.order.InvalidOrderStatus;
import com.nexora.orders.exception.order.OrderNotFound;
import com.nexora.orders.history.model.OrderHistory;
import com.nexora.orders.history.service.OrderHistoryService;
import com.nexora.orders.kafka.enums.EventType;
import com.nexora.orders.kafka.producer.OrderEventProducer;
import com.nexora.orders.kafka.producer.PaymentRequestEventProducer;
import com.nexora.orders.order.enums.OrderStatus;
import com.nexora.orders.order.model.Orders;
import com.nexora.orders.order.repository.OrderRepository;
import com.nexora.orders.orderItems.model.OrderItem;
import com.nexora.orders.request.order.CreateOrderRequest;
import com.nexora.orders.request.order.PaymentRequest;
import com.nexora.orders.request.orderItems.OrderItemRequest;
import com.nexora.orders.response.SuccessResponse;
import com.nexora.orders.response.order.OrderResponse;
import com.nexora.orders.response.order.VariantPriceResponse;
import com.nexora.orders.utility.GlobalUtility;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {
    private static final Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderHistoryService orderHistoryService;

    @Autowired
    private UserClient userClient;

    @Autowired
    private ProductClient productClient;

    @Autowired
    private OrderEventProducer orderCreatedEventProducer;

    @Autowired
    private PaymentRequestEventProducer paymentRequestEventProducer;

    @Override
    public OrderResponse createOrder(CreateOrderRequest orderRequest) {
        logger.info("Entering createOrder for userUid: {}", orderRequest.userProfileUid());
        userClient.isUserExists(orderRequest.userProfileUid());
        logger.info("User existence verified for userUid: {}", orderRequest.userProfileUid());
        Orders.OrdersBuilder order = Orders.builder().userProfileUid(orderRequest.userProfileUid());
        List<VariantPriceResponse> productResponse = productClient.getProducts(orderRequest.items());
        System.out.println(productResponse);
        logger.info("Fetched product pricing for {} items", orderRequest.items().size());
        Map<String, Double> variantPriceResponse = productResponse.stream().collect(Collectors.toMap(VariantPriceResponse::variantUid, VariantPriceResponse::price));
        List<OrderItem> orderItems = createOrderItems(variantPriceResponse, orderRequest.items());

        Double totalAmount = orderItems.stream()
                .mapToDouble(item -> item.getPrice() * item.getQuantity())
                .sum();
        logger.info("Calculated total order amount: {}", totalAmount);
        order.totalAmount(totalAmount);
        order.items(orderItems);
        Orders toSaveOrder = order.build();
        orderItems.forEach(orderItem -> orderItem.setOrder(toSaveOrder));

        Orders savedOrder = orderRepository.save(toSaveOrder);
        logger.info("Order created successfully with orderUid: {}", savedOrder.getUid());
        OrderHistory orderHistory = GlobalUtility.convertFromArgsToOrderHistory(savedOrder.getUid(), orderRequest.userProfileUid());
        orderHistoryService.createOrderHistory(orderHistory);
        logger.info("Order history created for orderUid: {}", savedOrder.getUid());
        orderCreatedEventProducer.publishOrderEvent(OrderCreatedEvent.builder().orderUid(savedOrder.getUid())
                .amount(savedOrder.getTotalAmount())
                .eventType(EventType.ORDER_CREATED)
                .userUid(GlobalUtility.getLoggedInUserDetails().userUid())
                .email(GlobalUtility.getLoggedInUserDetails().username())
                .build());
        logger.info("Order created event published for orderUid: {}", savedOrder.getUid());
        return GlobalUtility.convertFromOrderToOrderResponse(savedOrder);
    }


    private List<OrderItem> createOrderItems(Map<String, Double> variantPriceResponse, List<OrderItemRequest> orderItemRequests) {
        logger.info("Creating order items for {} requested items", orderItemRequests.size());
        return orderItemRequests.stream().map(orderItem ->
                OrderItem.builder().productUid(orderItem.productUid()).quantity(orderItem.quantity())
                        .variantUid(orderItem.variantUid())
                        .price(variantPriceResponse.get(orderItem.variantUid())).build()).toList();
    }

    @Override
    @Transactional
    public List<OrderResponse> fetchOrder(String userProfileUid, String orderUid, Integer pageNo, Integer pageSize, String sortBy, String direction) {
        logger.info("Entering fetchOrder with orderUid: {}, pageNo: {}, pageSize: {}, sortBy: {}, direction: {}", orderUid, pageNo, pageSize, sortBy, direction);

        Optional<Orders> order = orderRepository.findByUidAndUserProfileUid(orderUid, userProfileUid);
        if (order.isPresent()) {
            logger.info("Order found for orderUid: {} and userProfileUid: {}", orderUid, userProfileUid);
            return List.of(GlobalUtility.convertFromOrderToOrderResponse(order.get()));
        }
        sortBy = sortBy == null ? "status" : sortBy;
        Pageable pageable = GlobalUtility.getPageable(pageNo, pageSize, sortBy, direction);

        logger.info("Fetching paginated orders for userProfileUid: {}", userProfileUid);
        Page<Orders> ordersPage = orderRepository.findByUserProfileUid(userProfileUid, pageable);

        if (ordersPage.isEmpty()) {
            logger.warn("No orders found for userProfileUid: {}", userProfileUid);
            throw new EmptyOrderList();
        }

        logger.info("Fetched {} orders successfully for userProfileUid: {}", ordersPage.getContent().size(), userProfileUid);
        return ordersPage.getContent().stream().map(GlobalUtility::convertFromOrderToOrderResponse).toList();


    }

    @Override
    public SuccessResponse createPayment(PaymentRequest paymentRequest) {
        Orders order = orderRepository.findByUid(paymentRequest.orderUid()).orElseThrow(() -> new OrderNotFound(paymentRequest.orderUid()));
        logger.info("Entering createPayment for orderUid: {}", paymentRequest.orderUid());
        String userUid = GlobalUtility.getLoggedInUserDetails().userUid();
        paymentRequestEventProducer.publishPaymentRequestEvent(PaymentRequestEvent.builder()
                .userUid(userUid)
                .amount(order.getTotalAmount())
                .orderUid(paymentRequest.orderUid())
                .currency(paymentRequest.currency())
                .paymentMethod(paymentRequest.paymentMethod())
                .eventType(EventType.PAYMENT_REQUEST)
                .build());
        logger.info("Payment request event published for orderUid: {}", paymentRequest.orderUid());
        return new SuccessResponse("Payment is requested for order uid " + paymentRequest.orderUid() + "", HttpStatus.OK.value(), LocalDateTime.now());
    }

    @Override
    public void updateOrderStatus(PaymentStatusEvent orderStatusEvent) {
        Orders order = orderRepository.findByUid(orderStatusEvent.getOrderUid()).orElseThrow(() -> new OrderNotFound(orderStatusEvent.getOrderUid()));
        if (!OrderStatus.isValid(orderStatusEvent.getOrderStatus())) {
            throw new InvalidOrderStatus(orderStatusEvent.getOrderStatus());
        }
        order.setStatus(OrderStatus.valueOf(orderStatusEvent.getOrderStatus().toUpperCase()));
        orderRepository.save(order);
    }

}