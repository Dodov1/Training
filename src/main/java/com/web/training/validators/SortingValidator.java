package com.web.training.validators;

public interface SortingValidator {

    boolean validateTrainingSorting(String sortBy, String orderBy);

    boolean validateUserSorting(String sortBy, String orderBy);

    boolean validateTrainerSorting(String sortBy, String orderBy);
}
