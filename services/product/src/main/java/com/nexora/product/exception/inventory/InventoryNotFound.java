package com.nexora.product.exception.inventory;

public class InventoryNotFound extends RuntimeException {
    public InventoryNotFound(String uid) {
        super("Inventory not found with uid " + uid + "");
    }
}
