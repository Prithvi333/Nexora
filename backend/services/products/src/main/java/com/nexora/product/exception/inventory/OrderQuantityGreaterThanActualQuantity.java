package com.nexora.product.exception.inventory;

public class OrderQuantityGreaterThanActualQuantity extends RuntimeException {
    public OrderQuantityGreaterThanActualQuantity(Integer orderQuantity, Integer actualQuantity) {
        super("Order quantity : " + orderQuantity + " must be less than than actual quantity : " + actualQuantity + "");
    }
}
