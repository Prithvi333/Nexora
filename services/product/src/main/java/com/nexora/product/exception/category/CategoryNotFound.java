package com.nexora.product.exception.category;

public class CategoryNotFound extends RuntimeException {
    public CategoryNotFound(String categoryUid) {
        super("Category not found with uid " + categoryUid + "");
    }
}
