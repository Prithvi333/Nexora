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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AddressServiceImpl implements AddressService {

    private static final Logger log = LoggerFactory.getLogger(AddressServiceImpl.class);

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private UserProfileRepository userProfileRepository;

    @Override
    @Transactional
    public AddressResponse createAddress(CreateAddressRequest createAddressRequest) {
        log.info("Request received to create address for UserProfile UID: {}", createAddressRequest.userProfileUid());

        UserProfile userProfile = userProfileRepository.findByUid(createAddressRequest.userProfileUid())
                .orElseThrow(() -> {
                    log.error("Failed to create address: UserProfile not found with UID: {}", createAddressRequest.userProfileUid());
                    return new UserProfileNotFound(createAddressRequest.userProfileUid());
                });

        Address.AddressBuilder addressBuilder = GlobalUtils.convertFromAddressRequestToAddress(createAddressRequest);
        addressBuilder.userUid(userProfile.getUserUid());

        Address addressToSave = addressBuilder.build();
        userProfile.getAddresses().add(addressToSave);
        userProfileRepository.save(userProfile);

        log.info("Address created successfully with UID: {}", addressToSave.getUid());
        return GlobalUtils.convertFromAddressToAddressResponse(addressToSave);
    }

    @Override
    public SuccessResponse<String> updateAddress(UpdateAddressRequest updateAddressRequest) {
        log.info("Processing update request for Address UID: {}", updateAddressRequest.addressUid());
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
        log.info("Address with UID: {} updated successfully", updateAddressRequest.addressUid());

        return new SuccessResponse<>("Address had been updated successfully", HttpStatus.OK.value(), LocalDateTime.now());
    }

    @Override
    public List<AddressResponse> fetchAddress(String addressUid, Integer pageNo, Integer pageSize, String sortBy, String direction) {
        log.debug("Fetching address details. UID: {}, Page: {}, Size: {}", addressUid, pageNo, pageSize);

        Address address = getAddress(addressUid);
        if (address != null) {
            return List.of(GlobalUtils.convertFromAddressToAddressResponse(address));
        }

        String userUid = GlobalUtils.getLoggedInUserDetails().userUid();
        Pageable pageable = GlobalUtils.getPageable(pageNo, pageSize, sortBy, direction);
        Page<Address> addresses = addressRepository.findByUserUid(userUid, pageable);

        log.debug("Retrieved {} addresses for user context", addresses.getNumberOfElements());
        return addresses.getContent().stream().map(GlobalUtils::convertFromAddressToAddressResponse).toList();
    }

    @Override
    public SuccessResponse<String> deleteAddress(String addressUid) {
        log.info("Request received to delete address with UID: {}", addressUid);
        Address address = getAddress(addressUid);
        addressRepository.delete(address);
        log.info("Address UID: {} successfully deleted", addressUid);

        return new SuccessResponse<>("Address had been deleted successfully", HttpStatus.NO_CONTENT.value(), LocalDateTime.now());
    }

    private Address getAddress(String addressUid) {
        String userUid = GlobalUtils.getLoggedInUserDetails().userUid();
        log.trace("Validating ownership for Address UID: {} by User UID: {}", addressUid, userUid);
        return addressRepository.findByUidAndUserUid(addressUid, userUid)
                .orElseThrow(() -> {
                    log.warn("Address access denied or not found: UID {}", addressUid);
                    return new AddressNotFound(addressUid);
                });
    }
}