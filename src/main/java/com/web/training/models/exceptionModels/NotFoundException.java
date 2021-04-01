package com.web.training.models.exceptionModels;

public class NotFoundException extends RuntimeException {


    public NotFoundException(String message) {
        super(message);
    }

}
