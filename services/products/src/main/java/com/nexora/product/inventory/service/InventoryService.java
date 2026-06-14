package com.nexora.product.inventory.service;

import com.nexora.product.request.inventory.InventoryUpdateRequest;
import com.nexora.product.response.SuccessResponse;

public interface InventoryService {

    SuccessResponse updateInventory(InventoryUpdateRequest inventoryUpdateRequest);
}
