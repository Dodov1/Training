package com.web.training.models.exceptionModels;

public class NoUserFoundForTrainerException extends Exception {

    public NoUserFoundForTrainerException(String message) {
        super(message);
    }
}
