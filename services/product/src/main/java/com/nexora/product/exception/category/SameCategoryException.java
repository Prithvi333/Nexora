package com.nexora.product.exception.category;

public class SameCategoryException extends RuntimeException {
    public SameCategoryException() {
        super("Category and subcategory can't be same");
    }
}
