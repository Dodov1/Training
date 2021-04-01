package com.web.training.models.exceptionModels;

public class NoTrainerFoundForUserException extends Exception{

    public NoTrainerFoundForUserException(String message) {
        super(message);
    }
}
