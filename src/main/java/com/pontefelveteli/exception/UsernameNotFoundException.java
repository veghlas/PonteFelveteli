package com.pontefelveteli.exception;

public class UsernameNotFoundException extends RuntimeException {

    private String email;
    public UsernameNotFoundException(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }
}
