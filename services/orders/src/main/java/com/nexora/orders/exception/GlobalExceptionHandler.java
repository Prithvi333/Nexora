package com.nexora.orders.exception;

import com.nexora.orders.exception.history.EmptyOrderHistoryList;
import com.nexora.orders.exception.order.EmptyOrderList;
import com.nexora.orders.exception.order.OrderNotFound;
import com.nexora.orders.exception.orderItems.EmptyOrderItemList;
import com.nexora.orders.exception.orderItems.OrderItemNotFound;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(OrderNotFound.class)
    public ResponseEntity<ErrorResponse> handleOrderNotFound(OrderNotFound orderNotFound) {
        return new ResponseEntity<>(new ErrorResponse(orderNotFound.getMessage(), HttpStatus.NOT_FOUND.value(), LocalDateTime.now()), HttpStatus.NOT_FOUND);
    }


    @ExceptionHandler(EmptyOrderList.class)
    public ResponseEntity<ErrorResponse> handleEmptyOrderList(EmptyOrderList emptyOrderList) {
        return new ResponseEntity<>(new ErrorResponse(emptyOrderList.getMessage(), HttpStatus.BAD_REQUEST.value(), LocalDateTime.now()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EmptyOrderHistoryList.class)
    public ResponseEntity<ErrorResponse> handleEmptyOrderHistoryList(EmptyOrderHistoryList emptyOrderHistoryList) {
        return new ResponseEntity<>(new ErrorResponse(emptyOrderHistoryList.getMessage(), HttpStatus.BAD_REQUEST.value(), LocalDateTime.now()), HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(EmptyOrderItemList.class)
    public ResponseEntity<ErrorResponse> handleEmptyOrderItemList(EmptyOrderItemList emptyOrderItemList) {
        return new ResponseEntity<>(new ErrorResponse(emptyOrderItemList.getMessage(), HttpStatus.BAD_REQUEST.value(), LocalDateTime.now()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(OrderItemNotFound.class)
    public ResponseEntity<ErrorResponse> handleEmptyOrderItemNotFound(OrderItemNotFound orderItemNotFound) {
        return new ResponseEntity<>(new ErrorResponse(orderItemNotFound.getMessage(), HttpStatus.NOT_FOUND.value(), LocalDateTime.now()), HttpStatus.NOT_FOUND);
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidation(
            MethodArgumentNotValidException ex
    ) {

        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getFieldErrors()
                .forEach(error ->
                        errors.put(
                                error.getField(),
                                error.getDefaultMessage()
                        )
                );
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(errors);
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFound(
            NoHandlerFoundException ex
    ) {

        ErrorResponse response = new ErrorResponse(
                ex.getMessage(),
                HttpStatus.NOT_FOUND.value(),
                LocalDateTime.now()
        );

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(response);
    }

}
