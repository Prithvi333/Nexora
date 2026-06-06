package com.nexora.user.request.preference;

import com.nexora.user.preference.enums.CurrencyType;
import com.nexora.user.preference.enums.Language;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateUserPreferenceRequest(
        @NotBlank
        String userUid,

        @NotNull
        Language language,

        @NotNull
        CurrencyType currency,

        Boolean emailNotifications,

        Boolean smsNotifications,

        Boolean pushNotifications) {
}
