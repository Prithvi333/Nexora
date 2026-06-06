package com.nexora.user.address.controller;

import com.nexora.user.address.service.AddressService;
import com.nexora.user.request.address.CreateAddressRequest;
import com.nexora.user.request.address.UpdateAddressRequest;
import com.nexora.user.response.SuccessResponse;
import com.nexora.user.response.address.AddressResponse;
import com.nexora.user.utility.constants.IUrls;
import jakarta.validation.Valid;
import org.apache.kafka.common.protocol.types.Field;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(IUrls.USER + IUrls.ADDRESS)
public class AddressController {

    @Autowired
    private AddressService addressService;

    @PostMapping
    public ResponseEntity<AddressResponse> createAddress(@Valid @RequestBody CreateAddressRequest createAddressRequest) {
        return new ResponseEntity<>(addressService.createAddress(createAddressRequest), HttpStatus.CREATED);
    }


    @PutMapping
    public ResponseEntity<SuccessResponse<String>> updateAddress(@Valid @RequestBody UpdateAddressRequest updateAddressRequest) {
        return new ResponseEntity<>(addressService.updateAddress(updateAddressRequest), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<AddressResponse> fetchAddress(@RequestParam("addressUid") String addressUid) {
        return new ResponseEntity<>(addressService.fetchAddress(addressUid), HttpStatus.OK);
    }

    @DeleteMapping
    public ResponseEntity<SuccessResponse<String>> deleteAddress(@RequestParam("addressUid") String addressUid) {
        return new ResponseEntity<>(addressService.deleteAddress(addressUid), HttpStatus.NO_CONTENT);
    }

}
