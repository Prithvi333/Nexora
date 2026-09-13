package com.nexora.product.exception.product;

public class ProductOrVariantNotFound extends RuntimeException {
    public ProductOrVariantNotFound() {
        super("Either product or variant not found");
    }
}
