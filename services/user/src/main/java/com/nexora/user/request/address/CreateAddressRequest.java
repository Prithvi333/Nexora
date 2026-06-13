package com.nexora.user.request.address;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record CreateAddressRequest(

        @NotBlank(message = "user profile uid is mandatory")
        String userProfileUid,

        @NotBlank
        @Size(max = 150)
        String addressLine,

        @NotBlank
        @Size(max = 50)
        String city,

        @NotBlank
        @Size(max = 50)
        String state,

        @NotBlank
        @Size(max = 50)
        String country,

        @NotBlank
        @Pattern(
                regexp = "^[1-9][0-9]{5}$",
                message = "Invalid PIN code"
        )
        String postalCode,

        Boolean defaultAddress
) {
}
