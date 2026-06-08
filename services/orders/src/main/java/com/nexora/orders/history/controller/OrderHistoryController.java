package com.nexora.orders.history.controller;

import com.nexora.orders.history.service.OrderHistoryService;
import com.nexora.orders.response.history.OrderHistoryResponse;
import com.nexora.orders.utility.constants.IUrls;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(IUrls.USER + IUrls.HISTORY)
public class OrderHistoryController {

    @Autowired
    private OrderHistoryService orderHistoryService;

    @GetMapping
    public ResponseEntity<List<OrderHistoryResponse>> getAllOrderHistory(@RequestParam String orderUid, @RequestParam(required = false) Integer pageNo, @RequestParam(required = false) Integer pageSize, @RequestParam(required = false) String sortBy, @RequestParam(required = false) String direction) {
        return new ResponseEntity<>(orderHistoryService.orderHistoryList(orderUid, pageNo, pageSize, sortBy, direction), HttpStatus.OK);
    }
}
