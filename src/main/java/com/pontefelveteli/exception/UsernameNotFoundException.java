package com.pontefelveteli.exception;

public class UsernameNotFoundException extends RuntimeException {

    private String name;
    public UsernameNotFoundException(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
