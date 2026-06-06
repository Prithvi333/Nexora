package com.nexora.product.exception.inventory;

public class ReservedQuantityGreaterThanActualQuantity extends RuntimeException {
    public ReservedQuantityGreaterThanActualQuantity() {
        super("Reserved quantity must not be greater than the actual quantity");
    }
}
