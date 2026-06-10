package com.nexora.orders.orderItems.controller;
import com.nexora.orders.orderItems.service.OrderItemService;
import com.nexora.orders.request.orderItems.UpdateOrderItemRequest;
import com.nexora.orders.response.SuccessResponse;
import com.nexora.orders.response.orderItems.OrderItemResponse;
import com.nexora.orders.utility.constants.IUrls;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController
@RequestMapping(IUrls.USER + IUrls.ITEM)
public class OrderItemController {
    private static final Logger logger = LoggerFactory.getLogger(OrderItemController.class);

    @Autowired
    private OrderItemService orderItemService;

    @GetMapping
    public ResponseEntity<List<OrderItemResponse>> fetchOrderItems(@RequestParam(required = false) String orderUid, @RequestParam(required = false) Integer pageNo, @RequestParam(required = false) Integer pageSize, @RequestParam(required = false) String sortBy, @RequestParam(required = false) String direction) {
        logger.info("Received request to fetch order items with orderUid: {}, pageNo: {}, pageSize: {}, sortBy: {}, direction: {}", orderUid, pageNo, pageSize, sortBy, direction);
        ResponseEntity<List<OrderItemResponse>> response = new ResponseEntity<>(orderItemService.fetchOrderItems(orderUid, pageNo, pageSize, sortBy, direction), HttpStatus.OK);
        logger.info("Order items fetched successfully");
        return response;
    }

    @PutMapping
    public ResponseEntity<SuccessResponse> updateOrderItem(@Valid @RequestBody UpdateOrderItemRequest updateOrderItemRequest) {
        logger.info("Received request to update order item with orderItemUid: {}", updateOrderItemRequest.orderItemUid());
        ResponseEntity<SuccessResponse> response = new ResponseEntity<>(orderItemService.updateOrderItem(updateOrderItemRequest), HttpStatus.OK);
        logger.info("Order item updated successfully with orderItemUid: {}", updateOrderItemRequest.orderItemUid());
        return response;
    }

    @DeleteMapping
    public ResponseEntity<SuccessResponse> deleteOrderItem(@RequestParam("itemUid") String itemUid) {
        logger.info("Received request to delete order item with itemUid: {}", itemUid);
        ResponseEntity<SuccessResponse> response = new ResponseEntity<>(orderItemService.deleteOrderItem(itemUid), HttpStatus.NO_CONTENT);
        logger.info("Order item deleted successfully with itemUid: {}", itemUid);
        return response;
    }

}