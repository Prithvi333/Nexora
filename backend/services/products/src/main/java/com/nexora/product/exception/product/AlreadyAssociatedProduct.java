package com.nexora.product.exception.product;

public class AlreadyAssociatedProduct extends RuntimeException {

    public AlreadyAssociatedProduct(String name, String brand, String category) {
        super("Product with name " + name + " and brand " + brand + " already associated with category " + category + "");
    }
}
