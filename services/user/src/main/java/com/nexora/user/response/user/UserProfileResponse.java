package com.nexora.user.response.user;

import com.nexora.user.response.address.AddressResponse;
import com.nexora.user.response.preference.UserPreferenceResponse;
import lombok.Builder;

import java.util.List;

@Builder
public record UserProfileResponse(

        String userUid,

        String firstName,

        String lastName,

        String email,

        String phoneNumber,

        String profileImageUrl,

        String bio,

        List<AddressResponse> addresses,

        List<UserPreferenceResponse> preferences

) {
}