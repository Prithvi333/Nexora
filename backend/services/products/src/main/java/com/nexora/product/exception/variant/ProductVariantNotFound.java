package com.nexora.product.exception.variant;

public class ProductVariantNotFound extends RuntimeException {
    public ProductVariantNotFound(String uid) {
        super("Product variant not found with uid " + uid + "");
    }
}
