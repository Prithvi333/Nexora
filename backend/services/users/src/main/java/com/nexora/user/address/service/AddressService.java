package com.nexora.user.address.service;

import com.nexora.user.request.address.CreateAddressRequest;
import com.nexora.user.request.address.UpdateAddressRequest;
import com.nexora.user.response.SuccessResponse;
import com.nexora.user.response.address.AddressResponse;

import java.util.List;

public interface AddressService {

    AddressResponse createAddress(CreateAddressRequest createAddressRequest);

    SuccessResponse<String> updateAddress(UpdateAddressRequest updateAddressRequest);

    List<AddressResponse> fetchAddress(String addressUid, Integer pageNo, Integer pageSize, String sortBy, String direction);

    SuccessResponse<String> deleteAddress(String addressUid);

}
