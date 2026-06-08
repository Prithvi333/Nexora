package com.nexora.orders.orderItems.controller;

import com.nexora.orders.orderItems.service.OrderItemService;
import com.nexora.orders.request.orderItems.UpdateOrderItemRequest;
import com.nexora.orders.response.SuccessResponse;
import com.nexora.orders.response.orderItems.OrderItemResponse;
import com.nexora.orders.utility.constants.IUrls;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(IUrls.USER + IUrls.ITEM)
public class OrderItemController {

    @Autowired
    private OrderItemService orderItemService;

    @GetMapping
    public ResponseEntity<List<OrderItemResponse>> fetchOrderItems(@RequestParam(required = false) String orderUid, @RequestParam(required = false) Integer pageNo, @RequestParam(required = false) Integer pageSize, @RequestParam(required = false) String sortBy, @RequestParam(required = false) String direction) {
        return new ResponseEntity<>(orderItemService.fetchOrderItems(orderUid, pageNo, pageSize, sortBy, direction), HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<SuccessResponse> updateOrderItem(@Valid @RequestBody UpdateOrderItemRequest updateOrderItemRequest) {
        return new ResponseEntity<>(orderItemService.updateOrderItem(updateOrderItemRequest), HttpStatus.OK);
    }

    @DeleteMapping
    public ResponseEntity<SuccessResponse> deleteOrderItem(@RequestParam("itemUid") String itemUid) {
        return new ResponseEntity<>(orderItemService.deleteOrderItem(itemUid), HttpStatus.NO_CONTENT);
    }

}
