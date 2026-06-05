package com.nexora.product.inventory.controller;

import com.nexora.product.inventory.service.InventoryService;
import com.nexora.product.request.inventory.InventoryUpdateRequest;
import com.nexora.product.response.SuccessResponse;
import com.nexora.product.utility.constants.IUrls;
import jakarta.validation.Valid;
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

    @Autowired
    private InventoryService inventoryService;

    @PutMapping
    public ResponseEntity<SuccessResponse> updateInventory(@Valid @RequestBody InventoryUpdateRequest inventoryUpdateRequest) {
        return new ResponseEntity<>(inventoryService.updateInventory(inventoryUpdateRequest), HttpStatus.OK);
    }

}
