package com.web.training.models.exceptionModels;

public class InvalidSortingTypeException extends RuntimeException {


    public InvalidSortingTypeException(String message) {
        super(message);
    }
}
