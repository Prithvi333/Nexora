package com.nexora.user.utility;

import com.nexora.user.address.model.Address;
import com.nexora.user.preference.model.UserPreference;
import com.nexora.user.profile.model.UserProfile;
import com.nexora.user.request.address.CreateAddressRequest;
import com.nexora.user.request.preference.CreateUserPreferenceRequest;
import com.nexora.user.request.user.UserCreationRequest;
import com.nexora.user.response.address.AddressResponse;
import com.nexora.user.response.preference.UserPreferenceResponse;
import com.nexora.user.response.user.UserProfileResponse;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class GlobalUtils {

    public static Pageable getPageable(Integer pageNo, Integer pageSize, String sortBy, String direction) {
        pageSize = pageSize == null ? 5 : pageSize;
        pageNo = pageNo == null ? 0 : pageNo;
        Sort sort = direction == null ? Sort.by(sortBy).ascending() : direction.equals("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        return PageRequest.of(pageNo, pageSize, sort);
    }

    public static UserProfile convertFromUserProfileRequestToUserProfile(UserCreationRequest userCreationRequest) {

        return UserProfile.builder().email(userCreationRequest.email())
                .firstName(userCreationRequest.userName())
                .userUid(userCreationRequest.userUid())
                .build();

    }

    public static UserProfileResponse convertFromUserProfileToUserProfileResponse(UserProfile userProfile) {
        return UserProfileResponse.builder().firstName(userProfile.getFirstName())
                .lastName(userProfile.getLastName())
                .email(userProfile.getEmail())
                .userUid(userProfile.getUserUid())
                .profileImageUrl(userProfile.getProfileImageUrl())
                .bio(userProfile.getBio())
                .phoneNumber(userProfile.getPhoneNumber())
                .preferences(userProfile.getPreferences().stream().map(GlobalUtils::convertFromUserPreferenceToUserPreferenceResponse).toList())
                .addresses(userProfile.getAddresses().stream().map(GlobalUtils::convertFromAddressToAddressResponse).toList())
                .build();
    }

    public static UserPreferenceResponse convertFromUserPreferenceToUserPreferenceResponse(UserPreference userPreference) {
        return UserPreferenceResponse.builder()
                .userPreferenceUid(userPreference.getUid())
                .currency(userPreference.getCurrency())
                .emailNotifications(userPreference.getEmailNotifications())
                .language(userPreference.getLanguage())
                .pushNotifications(userPreference.getPushNotifications())
                .smsNotifications(userPreference.getSmsNotifications())
                .build();
    }

    public static UserPreference.UserPreferenceBuilder convertFromUserPreferenceRequestToUserPreference(CreateUserPreferenceRequest userPreferenceRequest) {
        return UserPreference.builder().currency(userPreferenceRequest.currency())
                .language(userPreferenceRequest.language())
                .smsNotifications(userPreferenceRequest.smsNotifications())
                .pushNotifications(userPreferenceRequest.pushNotifications())
                .emailNotifications(userPreferenceRequest.emailNotifications());
    }


    public static Address.AddressBuilder convertFromAddressRequestToAddress(CreateAddressRequest addressRequest) {
        return Address.builder()
                .defaultAddress(false)
                .fullName(addressRequest.fullName())
                .state(addressRequest.state())
                .city(addressRequest.city())
                .postalCode(addressRequest.postalCode())
                .country(addressRequest.country())
                .addressLine(addressRequest.addressLine());
    }

    public static AddressResponse convertFromAddressToAddressResponse(Address address) {
        return AddressResponse.builder()
                .uid(address.getUid())
                .addressLine(address.getAddressLine())
                .state(address.getState())
                .city(address.getCity())
                .country(address.getCountry())
                .postalCode(address.getPostalCode())
                .isDefault(address.getDefaultAddress())
                .build();
    }
}
