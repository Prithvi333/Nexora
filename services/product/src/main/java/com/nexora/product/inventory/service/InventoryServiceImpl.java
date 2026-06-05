package com.nexora.product.inventory.service;

import com.nexora.product.exception.inventory.InventoryNotFound;
import com.nexora.product.inventory.model.Inventory;
import com.nexora.product.inventory.repository.InventoryRepository;
import com.nexora.product.request.inventory.InventoryUpdateRequest;
import com.nexora.product.response.SuccessResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
@Service
public class InventoryServiceImpl implements InventoryService {

    @Autowired
    private InventoryRepository inventoryRepository;

    @Override
    public SuccessResponse updateInventory(InventoryUpdateRequest inventoryUpdateRequest) {

        Inventory inventory = inventoryRepository.findByUid(inventoryUpdateRequest.uid()).orElseThrow(() -> new InventoryNotFound(inventoryUpdateRequest.uid()));
        Integer updatedQuantity = inventory.getQuantity();
        if (inventoryUpdateRequest.quantity() != null) {
            updatedQuantity = inventoryUpdateRequest.quantity();
            inventory.setQuantity(updatedQuantity);
        }

        if (inventoryUpdateRequest.reserved() != null && inventoryUpdateRequest.reserved() <= updatedQuantity) {
            inventory.setReservedQuantity(inventoryUpdateRequest.reserved());
        }

        inventoryRepository.save(inventory);

        return new SuccessResponse("Inventory has been updated successfully", HttpStatus.OK.value(), LocalDateTime.now());
    }
}
