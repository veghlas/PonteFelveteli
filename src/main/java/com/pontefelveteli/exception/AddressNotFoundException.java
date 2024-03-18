package com.pontefelveteli.exception;


public class AddressNotFoundException extends RuntimeException {
    private Integer addressId;

    private String email;

    public AddressNotFoundException(Integer addressId, String email) {
        this.addressId = addressId;
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public Integer getAddressId() {
        return addressId;
    }
}
