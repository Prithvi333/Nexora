package com.nexora.product.exception;

import com.nexora.product.exception.S3.S3Exception;
import com.nexora.product.exception.category.CategoryNotFound;
import com.nexora.product.exception.category.EmptyCategoryList;
import com.nexora.product.exception.category.SameCategoryException;
import com.nexora.product.exception.image.ProductImageNotFound;
import com.nexora.product.exception.inventory.InventoryException;
import com.nexora.product.exception.inventory.InventoryNotFound;
import com.nexora.product.exception.inventory.OrderQuantityGreaterThanActualQuantity;
import com.nexora.product.exception.inventory.ReservedQuantityGreaterThanActualQuantity;
import com.nexora.product.exception.product.AlreadyAssociatedProduct;
import com.nexora.product.exception.product.EmptyProductList;
import com.nexora.product.exception.product.ProductNotFound;
import com.nexora.product.exception.product.ProductOrVariantNotFound;
import com.nexora.product.exception.variant.EmptyProductVariantList;
import com.nexora.product.exception.variant.ProductVariantAlreadyAssociated;
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
        return new ResponseEntity<>(new ErrorResponse(productNotFound.getMessage(), HttpStatus.NOT_FOUND.value(), LocalDateTime.now()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(EmptyProductList.class)
    public ResponseEntity<ErrorResponse> handleEmptyProductList(EmptyProductList emptyProductList) {
        return new ResponseEntity<>(new ErrorResponse(emptyProductList.getMessage(), HttpStatus.BAD_REQUEST.value(), LocalDateTime.now()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ProductVariantNotFound.class)
    public ResponseEntity<ErrorResponse> handleProductVariantNotFound(ProductVariantNotFound productVariantNotFound) {
        return new ResponseEntity<>(new ErrorResponse(productVariantNotFound.getMessage(), HttpStatus.NOT_FOUND.value(), LocalDateTime.now()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(EmptyProductVariantList.class)
    public ResponseEntity<ErrorResponse> handleEmptyProductProductVariant(EmptyProductVariantList emptyProductVariantList) {
        return new ResponseEntity<>(new ErrorResponse(emptyProductVariantList.getMessage(), HttpStatus.BAD_REQUEST.value(), LocalDateTime.now()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InventoryNotFound.class)
    public ResponseEntity<ErrorResponse> handleInventoryNotFound(InventoryNotFound inventoryNotFound) {
        return new ResponseEntity<>(new ErrorResponse(inventoryNotFound.getMessage(), HttpStatus.NOT_FOUND.value(), LocalDateTime.now()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(S3Exception.class)
    public ResponseEntity<ErrorResponse> handleS3Exception(S3Exception s3Exception) {
        return new ResponseEntity<>(new ErrorResponse(s3Exception.getMessage(), HttpStatus.BAD_REQUEST.value(), LocalDateTime.now()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ProductImageNotFound.class)
    public ResponseEntity<ErrorResponse> handleProductImageNotFound(ProductImageNotFound productImageNotFound) {
        return new ResponseEntity<>(new ErrorResponse(productImageNotFound.getMessage(), HttpStatus.NOT_FOUND.value(), LocalDateTime.now()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(SameCategoryException.class)
    public ResponseEntity<ErrorResponse> handleSameCategoryException(SameCategoryException sameCategoryException) {
        return new ResponseEntity<>(new ErrorResponse(sameCategoryException.getMessage(), HttpStatus.BAD_REQUEST.value(), LocalDateTime.now()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AlreadyAssociatedProduct.class)
    public ResponseEntity<ErrorResponse> handleAlreadyAssociatedProduct(AlreadyAssociatedProduct alreadyAssociatedProduct) {
        return new ResponseEntity<>(new ErrorResponse(alreadyAssociatedProduct.getMessage(), HttpStatus.BAD_REQUEST.value(), LocalDateTime.now()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ProductVariantAlreadyAssociated.class)
    public ResponseEntity<ErrorResponse> handleProductVariantAlreadyAssociated(ProductVariantAlreadyAssociated productVariantAlreadyAssociated) {
        return new ResponseEntity<>(new ErrorResponse(productVariantAlreadyAssociated.getMessage(), HttpStatus.BAD_REQUEST.value(), LocalDateTime.now()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ReservedQuantityGreaterThanActualQuantity.class)
    public ResponseEntity<ErrorResponse> handleQuantityException(ReservedQuantityGreaterThanActualQuantity reservedQuantityGreaterThanActualQuantity) {
        return new ResponseEntity<>(new ErrorResponse(reservedQuantityGreaterThanActualQuantity.getMessage(), HttpStatus.BAD_REQUEST.value(), LocalDateTime.now()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ProductOrVariantNotFound.class)
    public ResponseEntity<ErrorResponse> handleProductOrVariantNotFound(ProductOrVariantNotFound productOrVariantNotFound) {
        return new ResponseEntity<>(new ErrorResponse(productOrVariantNotFound.getMessage(), HttpStatus.NOT_FOUND.value(), LocalDateTime.now()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(OrderQuantityGreaterThanActualQuantity.class)
    public ResponseEntity<ErrorResponse> handleQuantityMismatch(OrderQuantityGreaterThanActualQuantity orderQuantityGreaterThanActualQuantity) {
        return new ResponseEntity<>(new ErrorResponse(orderQuantityGreaterThanActualQuantity.getMessage(), HttpStatus.BAD_REQUEST.value(), LocalDateTime.now()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InventoryException.class)
    public ResponseEntity<ErrorResponse> handleInventoryException(InventoryException inventoryException) {
        return new ResponseEntity<>(new ErrorResponse(inventoryException.getMessage(), HttpStatus.BAD_REQUEST.value(), LocalDateTime.now()), HttpStatus.BAD_REQUEST);
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
