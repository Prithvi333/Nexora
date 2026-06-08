package com.nexora.orders.history.controller;

import com.nexora.orders.history.service.AdminOrderHistoryService;
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
@RequestMapping(IUrls.ADMIN + IUrls.HISTORY)
public class AdminOrderHistoryController {

    @Autowired
    private AdminOrderHistoryService adminOrderHistoryService;


    @GetMapping
    public ResponseEntity<List<OrderHistoryResponse>> getOrderHistory(@RequestParam(required = false) Integer pageNo, @RequestParam(required = false) Integer pageSize, @RequestParam(required = false) String sortBy, @RequestParam(required = false) String direction) {
        return new ResponseEntity<>(adminOrderHistoryService.fetchAllOrderHistory(pageNo, pageSize, sortBy, direction), HttpStatus.OK);
    }

}
