package com.pontefelveteli.exception;

public class UserNotFoundById extends RuntimeException {

    private Integer userId;
    public UserNotFoundById(Integer userId) {
        this.userId = userId;
    }

    public Integer getUserId() {
        return userId;
    }
}
