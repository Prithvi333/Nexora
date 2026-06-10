package com.nexora.product.inventory.service;

import com.nexora.product.exception.inventory.InventoryNotFound;
import com.nexora.product.inventory.model.Inventory;
import com.nexora.product.inventory.repository.InventoryRepository;
import com.nexora.product.request.inventory.InventoryUpdateRequest;
import com.nexora.product.response.SuccessResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class InventoryServiceImpl implements InventoryService {

    private static final Logger log = LoggerFactory.getLogger(InventoryServiceImpl.class);

    @Autowired
    private InventoryRepository inventoryRepository;

    @Override
    public SuccessResponse updateInventory(InventoryUpdateRequest inventoryUpdateRequest) {
        log.info("Request received to update inventory for UID: {}", inventoryUpdateRequest.uid());

        Inventory inventory = inventoryRepository.findByUid(inventoryUpdateRequest.uid())
                .orElseThrow(() -> {
                    log.error("Inventory update aborted: Record not found for UID: {}", inventoryUpdateRequest.uid());
                    return new InventoryNotFound(inventoryUpdateRequest.uid());
                });

        Integer updatedQuantity = inventory.getQuantity();
        if (inventoryUpdateRequest.quantity() != null) {
            log.debug("Updating stock quantity from {} to {}", inventory.getQuantity(), inventoryUpdateRequest.quantity());
            updatedQuantity = inventoryUpdateRequest.quantity();
            inventory.setQuantity(updatedQuantity);
        }

        if (inventoryUpdateRequest.reserved() != null) {
            if (inventoryUpdateRequest.reserved() <= updatedQuantity) {
                log.debug("Updating reserved quantity from {} to {}", inventory.getReservedQuantity(), inventoryUpdateRequest.reserved());
                inventory.setReservedQuantity(inventoryUpdateRequest.reserved());
            } else {
                log.warn("Skipping reserved quantity update. Provided reserved quantity ({}) exceeds available stock quantity ({})",
                        inventoryUpdateRequest.reserved(), updatedQuantity);
            }
        }

        inventoryRepository.save(inventory);
        log.info("Inventory with UID: {} has been saved successfully", inventoryUpdateRequest.uid());

        return new SuccessResponse("Inventory has been updated successfully", HttpStatus.OK.value(), LocalDateTime.now());
    }
}