package com.nexora.product.exception.category;

public class EmptyCategoryList extends RuntimeException {
    public EmptyCategoryList() {
        super("Category list is empty");
    }
}
