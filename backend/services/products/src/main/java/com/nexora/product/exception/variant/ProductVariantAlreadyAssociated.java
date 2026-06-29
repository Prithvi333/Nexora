package com.nexora.product.exception.variant;

public class ProductVariantAlreadyAssociated extends RuntimeException {
    public ProductVariantAlreadyAssociated(String productName) {
        super("Product variant already associated with the " + productName + "");
    }
}
