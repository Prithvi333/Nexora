package com.nexora.user.response.address;


import lombok.Builder;

@Builder
public record AddressResponse(

        String uid,

        String addressLine,

        String city,

        String state,

        String country,

        String postalCode,

        Boolean isDefault

) {
}