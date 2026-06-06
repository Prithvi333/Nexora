package com.nexora.user.address.service;

import com.nexora.user.request.address.CreateAddressRequest;
import com.nexora.user.request.address.UpdateAddressRequest;
import com.nexora.user.response.SuccessResponse;
import com.nexora.user.response.address.AddressResponse;

public interface AddressService {

    AddressResponse createAddress(CreateAddressRequest createAddressRequest);

    SuccessResponse<String> updateAddress(UpdateAddressRequest updateAddressRequest);

    AddressResponse fetchAddress(String addressUid);

    SuccessResponse<String> deleteAddress(String addressUid);

}
