package com.nexora.user.address.service;

import com.nexora.user.address.model.Address;
import com.nexora.user.address.repository.AddressRepository;
import com.nexora.user.exception.address.AddressNotFound;
import com.nexora.user.exception.profile.UserProfileNotFound;
import com.nexora.user.profile.model.UserProfile;
import com.nexora.user.profile.repository.UserProfileRepository;
import com.nexora.user.request.address.CreateAddressRequest;
import com.nexora.user.request.address.UpdateAddressRequest;
import com.nexora.user.response.SuccessResponse;
import com.nexora.user.response.address.AddressResponse;
import com.nexora.user.utility.GlobalUtils;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AddressServiceImpl implements AddressService {

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private UserProfileRepository userProfileRepository;

    @Override
    @Transactional
    public AddressResponse createAddress(CreateAddressRequest createAddressRequest) {

        UserProfile userProfile = userProfileRepository.findByUid(createAddressRequest.userProfileUid()).orElseThrow(() -> new UserProfileNotFound(createAddressRequest.userProfileUid()));
        Address.AddressBuilder addressBuilder = GlobalUtils.convertFromAddressRequestToAddress(createAddressRequest);
        addressBuilder.userUid(userProfile.getUserUid());

        Address addressToSave = addressBuilder.build();
        userProfile.getAddresses().add(addressToSave);
        userProfileRepository.save(userProfile);
        return GlobalUtils.convertFromAddressToAddressResponse(addressToSave);
    }

    @Override
    public SuccessResponse<String> updateAddress(UpdateAddressRequest updateAddressRequest) {
        Address address = getAddress(updateAddressRequest.addressUid());

        if (updateAddressRequest.addressLine() != null && !updateAddressRequest.addressLine().isBlank()) {
            address.setAddressLine(updateAddressRequest.addressLine());
        }
        if (updateAddressRequest.city() != null && !updateAddressRequest.city().isBlank()) {
            address.setCity(updateAddressRequest.city());
        }
        if (updateAddressRequest.state() != null && !updateAddressRequest.state().isBlank()) {
            address.setState(updateAddressRequest.state());
        }

        if (updateAddressRequest.country() != null && !updateAddressRequest.country().isBlank()) {
            address.setCountry(updateAddressRequest.country());
        }
        if (updateAddressRequest.postalCode() != null && !updateAddressRequest.postalCode().isBlank()) {
            address.setPostalCode(updateAddressRequest.postalCode());
        }
        if (updateAddressRequest.defaultAddress() != null) {
            address.setDefaultAddress(updateAddressRequest.defaultAddress());
        }

        addressRepository.save(address);

        return new SuccessResponse<>("Address had been updated successfully", HttpStatus.OK.value(), LocalDateTime.now());

    }

    @Override
    public List<AddressResponse> fetchAddress(String addressUid, Integer pageNo, Integer pageSize, String sortBy, String direction) {
        Address address = getAddress(addressUid);
        if (address != null) {
            return List.of(GlobalUtils.convertFromAddressToAddressResponse(address));
        }
        String userUid = GlobalUtils.getLoggedInUserDetails().userUid();
        Pageable pageable = GlobalUtils.getPageable(pageNo, pageSize, sortBy, direction);
        Page<Address> addresses = addressRepository.findByUserUid(userUid, pageable);

        return addresses.getContent().stream().map(GlobalUtils::convertFromAddressToAddressResponse).toList();
    }

    @Override
    public SuccessResponse<String> deleteAddress(String addressUid) {
        Address address = getAddress(addressUid);
        addressRepository.delete(address);
        return new SuccessResponse<>("Address had been deleted successfully", HttpStatus.NO_CONTENT.value(), LocalDateTime.now());
    }

    private Address getAddress(String addressUid) {
        String userUid = GlobalUtils.getLoggedInUserDetails().userUid();
        return addressRepository.findByUidAndUserUid(addressUid, userUid).orElseThrow(() -> new AddressNotFound(addressUid));
    }
}
