package com.nexora.product.exception.product;

public class EmptyProductList extends RuntimeException {
    public EmptyProductList() {
        super("Empty product list");
    }
}
