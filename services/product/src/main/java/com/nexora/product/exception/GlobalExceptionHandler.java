package com.nexora.product.exception;

import com.nexora.product.exception.category.CategoryNotFound;
import com.nexora.product.exception.category.EmptyCategoryList;
import com.nexora.product.exception.inventory.InventoryNotFound;
import com.nexora.product.exception.product.EmptyProductList;
import com.nexora.product.exception.product.ProductNotFound;
import com.nexora.product.exception.variant.EmptyProductVariantList;
import com.nexora.product.exception.variant.ProductVariantNotFound;
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


    @ExceptionHandler(CategoryNotFound.class)
    public ResponseEntity<ErrorResponse> handleCategoryNotFound(CategoryNotFound categoryNotFound) {
        return new ResponseEntity<>(new ErrorResponse(categoryNotFound.getMessage(), HttpStatus.NOT_FOUND.value(), LocalDateTime.now()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(EmptyCategoryList.class)
    public ResponseEntity<ErrorResponse> handleEmptyCategoryList(EmptyCategoryList emptyCategoryList) {
        return new ResponseEntity<>(new ErrorResponse(emptyCategoryList.getMessage(), HttpStatus.BAD_REQUEST.value(), LocalDateTime.now()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ProductNotFound.class)
    public ResponseEntity<ErrorResponse> handleProductNotFound(ProductNotFound productNotFound) {
        return new ResponseEntity<>(new ErrorResponse(productNotFound.getMessage(), HttpStatus.NOT_FOUND.value(), LocalDateTime.now()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EmptyProductList.class)
    public ResponseEntity<ErrorResponse> handleEmptyProductList(EmptyProductList emptyProductList) {
        return new ResponseEntity<>(new ErrorResponse(emptyProductList.getMessage(), HttpStatus.BAD_REQUEST.value(), LocalDateTime.now()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ProductVariantNotFound.class)
    public ResponseEntity<ErrorResponse> handleProductVariantNotFound(ProductVariantNotFound productVariantNotFound) {
        return new ResponseEntity<>(new ErrorResponse(productVariantNotFound.getMessage(), HttpStatus.NOT_FOUND.value(), LocalDateTime.now()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EmptyProductVariantList.class)
    public ResponseEntity<ErrorResponse> handleEmptyProductProductVariant(EmptyProductVariantList emptyProductVariantList) {
        return new ResponseEntity<>(new ErrorResponse(emptyProductVariantList.getMessage(), HttpStatus.BAD_REQUEST.value(), LocalDateTime.now()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InventoryNotFound.class)
    public ResponseEntity<ErrorResponse> handleInventoryNotFound(InventoryNotFound inventoryNotFound) {
        return new ResponseEntity<>(new ErrorResponse(inventoryNotFound.getMessage(), HttpStatus.NOT_FOUND.value(), LocalDateTime.now()), HttpStatus.BAD_REQUEST);
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
