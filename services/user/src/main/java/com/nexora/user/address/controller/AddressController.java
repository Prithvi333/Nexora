package com.nexora.user.address.controller;
import com.nexora.user.address.service.AddressService;
import com.nexora.user.request.address.CreateAddressRequest;
import com.nexora.user.request.address.UpdateAddressRequest;
import com.nexora.user.response.SuccessResponse;
import com.nexora.user.response.address.AddressResponse;
import com.nexora.user.utility.constants.IUrls;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController
@RequestMapping(IUrls.USER + IUrls.ADDRESS)
public class AddressController {
    private static final Logger logger = LoggerFactory.getLogger(AddressController.class);

    @Autowired
    private AddressService addressService;

    @PostMapping
    @Operation(summary = "Create address", description = "User to create the user address")
    public ResponseEntity<AddressResponse> createAddress(@Valid @RequestBody CreateAddressRequest createAddressRequest) {
        logger.info("Received request to create address");
        ResponseEntity<AddressResponse> response = new ResponseEntity<>(addressService.createAddress(createAddressRequest), HttpStatus.CREATED);
        logger.info("Address created successfully");
        return response;
    }


    @PutMapping
    @Operation(summary = "Update address", description = "Used to update the address")
    public ResponseEntity<SuccessResponse<String>> updateAddress(@Valid @RequestBody UpdateAddressRequest updateAddressRequest) {
        logger.info("Received request to update address");
        ResponseEntity<SuccessResponse<String>> response = new ResponseEntity<>(addressService.updateAddress(updateAddressRequest), HttpStatus.OK);
        logger.info("Address updated successfully");
        return response;
    }

    @GetMapping
    @Operation(summary = "Fetch address", description = "Used to get the user address")
    public ResponseEntity<List<AddressResponse>> fetchAddress(@RequestParam(required = false) String addressUid, @RequestParam(required = false) Integer pageNo, @RequestParam(required = false) Integer pageSize, @RequestParam(required = false) String sortBy, @RequestParam(required = false) String direction) {
        logger.info("Received request to fetch address with addressUid: {}, pageNo: {}, pageSize: {}, sortBy: {}, direction: {}", addressUid, pageNo, pageSize, sortBy, direction);
        ResponseEntity<List<AddressResponse>> response = new ResponseEntity<>(addressService.fetchAddress(addressUid, pageNo, pageSize, sortBy, direction), HttpStatus.OK);
        logger.info("Address fetched successfully");
        return response;
    }

    @DeleteMapping
    @Operation(summary = "Delete address", description = "Used to delete the user address")
    public ResponseEntity<SuccessResponse<String>> deleteAddress(@RequestParam("addressUid") String addressUid) {
        logger.info("Received request to delete address with addressUid: {}", addressUid);
        ResponseEntity<SuccessResponse<String>> response = new ResponseEntity<>(addressService.deleteAddress(addressUid), HttpStatus.NO_CONTENT);
        logger.info("Address deleted successfully with addressUid: {}", addressUid);
        return response;
    }

}