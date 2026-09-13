package com.nexora.product.inventory.controller;
import com.nexora.product.inventory.service.InventoryService;
import com.nexora.product.request.inventory.InventoryUpdateRequest;
import com.nexora.product.response.SuccessResponse;
import com.nexora.product.utility.constants.IUrls;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@RestController
@RequestMapping(IUrls.ADMIN + IUrls.INVENTORY)
public class InventoryController {
    private static final Logger logger = LoggerFactory.getLogger(InventoryController.class);

    @Autowired
    private InventoryService inventoryService;

    @PutMapping
    @Operation(summary = "Update inventory",description = "Used to update the inventory")
    public ResponseEntity<SuccessResponse> updateInventory(@Valid @RequestBody InventoryUpdateRequest inventoryUpdateRequest) {
        logger.info("Received request to update inventory");
        ResponseEntity<SuccessResponse> response = new ResponseEntity<>(inventoryService.updateInventory(inventoryUpdateRequest), HttpStatus.OK);
        logger.info("Inventory updated successfully");
        return response;
    }

}