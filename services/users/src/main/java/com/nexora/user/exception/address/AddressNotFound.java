package com.nexora.user.exception.address;

public class AddressNotFound extends RuntimeException {
    public AddressNotFound(String uid) {
        super("Address not found with uid " + uid + "");
    }
}
