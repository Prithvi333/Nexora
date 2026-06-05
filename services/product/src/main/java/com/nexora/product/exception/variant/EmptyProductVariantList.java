package com.nexora.product.exception.variant;

public class EmptyProductVariantList extends RuntimeException {
    public EmptyProductVariantList() {
        super("Empty product variant list");
    }
}
