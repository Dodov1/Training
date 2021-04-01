package com.web.training.models.exceptionModels;

public class UserIsAlreadyTrainerException extends Exception {

    public UserIsAlreadyTrainerException(String message) {
        super(message);
    }
}
