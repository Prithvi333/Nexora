package com.nexora.user.request.address;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UpdateAddressRequest(


        @NotBlank(message = "Address uid is required")
        String addressUid,

        @Size(max = 150)
        String addressLine,

        @Size(max = 50)
        String city,

        @Size(max = 50)
        String state,

        @Size(max = 50)
        String country,

        @Pattern(
                regexp = "^[1-9][0-9]{5}$",
                message = "Invalid PIN code"
        )
        String postalCode,

        Boolean defaultAddress
) {
}
