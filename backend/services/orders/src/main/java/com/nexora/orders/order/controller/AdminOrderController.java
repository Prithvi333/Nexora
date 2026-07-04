package com.nexora.orders.order.controller;
import com.nexora.orders.order.service.AdminOrderService;
import com.nexora.orders.response.order.OrderResponse;
import com.nexora.orders.utility.constants.IUrls;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
@RestController
@RequestMapping(IUrls.ADMIN + IUrls.ORDER)
public class AdminOrderController {
    private static final Logger logger = LoggerFactory.getLogger(AdminOrderController.class);

    @Autowired
    private AdminOrderService adminOrderService;
    @GetMapping
    public ResponseEntity<List<OrderResponse>> fetchAllOrders(@RequestParam(required = false) Integer pageNo, @RequestParam(required = false) Integer pageSize, @RequestParam(required = false) String sortBy, @RequestParam(required = false) String direction) {
        logger.info("Received request to fetch all orders with pageNo: {}, pageSize: {}, sortBy: {}, direction: {}", pageNo, pageSize, sortBy, direction);
        ResponseEntity<List<OrderResponse>> response = new ResponseEntity<>(adminOrderService.fetchAllOrders(pageNo, pageSize, sortBy, direction), HttpStatus.OK);
        logger.info("All orders fetched successfully");
        return response;
    }

}