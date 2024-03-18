package com.pontefelveteli.exception;

import java.util.List;

public class EmailOrPhoneNumberIsRequiredException extends RuntimeException {
    private String email;
    private List<String> phoneNumbers;
    public EmailOrPhoneNumberIsRequiredException(String email, List<String> phoneNumbers) {
        this.email = email;
        this.phoneNumbers = phoneNumbers;
    }

    public String getEmail() {
        return email;
    }

    public List<String> getPhoneNumbers() {
        return phoneNumbers;
    }
}
