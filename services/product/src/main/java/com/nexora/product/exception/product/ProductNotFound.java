package com.nexora.product.exception.product;

public class ProductNotFound extends RuntimeException {
    public ProductNotFound(String productUid) {
        super("Product not found with uid " + productUid + "");
    }
}
