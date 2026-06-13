package com.nexora.user.request.preference;

import com.nexora.user.preference.enums.CurrencyType;
import com.nexora.user.preference.enums.Language;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateUserPreferenceRequest(
        @NotBlank
        String userProfileUid,

        @NotNull
        String language,

        @NotNull
        String currency,

        @NotNull
        Boolean defaultPreference,

        Boolean emailNotifications,

        Boolean smsNotifications,

        Boolean pushNotifications) {
}
