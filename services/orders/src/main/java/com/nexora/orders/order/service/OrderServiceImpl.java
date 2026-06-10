package com.nexora.orders.order.service;
import com.nexora.common.events.OrderCreatedEvent;
import com.nexora.common.events.PaymentRequestEvent;
import com.nexora.orders.config.feign.products.ProductClient;
import com.nexora.orders.config.feign.users.UserClient;
import com.nexora.orders.exception.order.EmptyOrderList;
import com.nexora.orders.history.model.OrderHistory;
import com.nexora.orders.history.service.OrderHistoryService;
import com.nexora.orders.kafka.enums.EventType;
import com.nexora.orders.kafka.producer.OrderCreatedEventProducer;
import com.nexora.orders.kafka.producer.PaymentRequestEventProducer;
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
    private OrderCreatedEventProducer orderCreatedEventProducer;

    @Autowired
    private PaymentRequestEventProducer paymentRequestEventProducer;

    @Override
    public OrderResponse createOrder(CreateOrderRequest orderRequest) {
        logger.info("Entering createOrder for userUid: {}", orderRequest.userUid());
        userClient.isUserExists(orderRequest.userUid());
        logger.info("User existence verified for userUid: {}", orderRequest.userUid());
        Orders.OrdersBuilder order = Orders.builder().userUid(orderRequest.userUid());
        List<VariantPriceResponse> productResponse = productClient.getProducts(orderRequest.items());
        logger.info("Fetched product pricing for {} items", orderRequest.items().size());
        Map<String, Double> variantPriceResponse = productResponse.stream().collect(Collectors.toMap(VariantPriceResponse::variantUid, VariantPriceResponse::price));
        List<OrderItem> orderItems = createOrderItems(variantPriceResponse, orderRequest.items());

        Double totalAmount = orderItems.stream()
                .mapToDouble(OrderItem::getPrice)
                .sum();
        logger.info("Calculated total order amount: {}", totalAmount);
        order.totalAmount(totalAmount);
        order.items(orderItems);
        Orders savedOrder = orderRepository.save(order.build());
        logger.info("Order created successfully with orderUid: {}", savedOrder.getUid());
        OrderHistory orderHistory = GlobalUtility.convertFromArgsToOrderHistory(savedOrder.getUid());
        orderHistoryService.createOrderHistory(orderHistory);
        logger.info("Order history created for orderUid: {}", savedOrder.getUid());
        orderCreatedEventProducer.publishOrderCreatedEvent(OrderCreatedEvent.builder().orderUid(savedOrder.getUid())
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
    public List<OrderResponse> fetchOrder(String orderUid, Integer pageNo, Integer pageSize, String sortBy, String direction) {
        logger.info("Entering fetchOrder with orderUid: {}, pageNo: {}, pageSize: {}, sortBy: {}, direction: {}", orderUid, pageNo, pageSize, sortBy, direction);
        String userUid = GlobalUtility.getLoggedInUserDetails().userUid();
        Optional<Orders> order = orderRepository.findByUidAndUserUid(orderUid, userUid);
        if (order.isPresent()) {
            logger.info("Order found for orderUid: {} and userUid: {}", orderUid, userUid);
            return List.of(GlobalUtility.convertFromOrderToOrderResponse(order.get()));
        }

        Pageable pageable = GlobalUtility.getPageable(pageNo, pageSize, sortBy, direction);

        logger.info("Fetching paginated orders for userUid: {}", userUid);
        Page<Orders> ordersPage = orderRepository.findByUserUid(userUid, pageable);

        if (ordersPage.isEmpty()) {
            logger.warn("No orders found for userUid: {}", userUid);
            throw new EmptyOrderList();
        }

        logger.info("Fetched {} orders successfully for userUid: {}", ordersPage.getContent().size(), userUid);
        return ordersPage.getContent().stream().map(GlobalUtility::convertFromOrderToOrderResponse).toList();


    }

    @Override
    public SuccessResponse createPayment(PaymentRequest paymentRequest) {
        logger.info("Entering createPayment for orderUid: {} and userUid: {}", paymentRequest.orderUid(), paymentRequest.userUid());
        paymentRequestEventProducer.publishPaymentRequestEvent(PaymentRequestEvent.builder()
                .userUid(paymentRequest.userUid())
                .orderUid(paymentRequest.orderUid())
                .currency(paymentRequest.currency())
                .paymentMethod(paymentRequest.paymentMethod())
                .build());
        logger.info("Payment request event published for orderUid: {}", paymentRequest.orderUid());
        return new SuccessResponse("Payment is requresed for order uid " + paymentRequest.orderUid() + "", HttpStatus.OK.value(), LocalDateTime.now());
    }

}