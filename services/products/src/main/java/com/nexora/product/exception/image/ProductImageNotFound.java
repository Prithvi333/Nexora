package com.nexora.product.exception.image;

public class ProductImageNotFound extends RuntimeException {
    public ProductImageNotFound(String uid) {
        super("Product image not found with uid " + uid + "");
    }
}
