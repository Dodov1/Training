package com.web.training.models.exceptionModels;

public class UserNotVerifiedException extends RuntimeException {

    public UserNotVerifiedException(String message) {
        super(message);
    }
}
