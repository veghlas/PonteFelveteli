package com.pontefelveteli.exception;

public class YouHaveLoggedOutException extends RuntimeException {
    private String username = "UnknownUser";

    public YouHaveLoggedOutException() {
    }

    public String getUsername() {
        return username;
    }
}
