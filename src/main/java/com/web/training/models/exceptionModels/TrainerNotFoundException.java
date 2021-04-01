package com.web.training.models.exceptionModels;

public class TrainerNotFoundException extends RuntimeException {

    public TrainerNotFoundException(String message) {
        super(message);
    }
}
