package com.web.training.validators.impl;

import com.web.training.models.enums.TrainerSortByEnum;
import com.web.training.models.enums.TrainingSortByEnum;
import com.web.training.models.enums.UserSortByEnum;
import com.web.training.validators.SortingValidator;
import org.springframework.stereotype.Component;

import java.util.Arrays;

import static com.web.training.config.appConstants.AppConstants.ASCENDING_ORDER_NAME;
import static com.web.training.config.appConstants.AppConstants.DESCENDING_ORDER_NAME;

@Component
public class SortingValidatorImpl implements SortingValidator {

    @Override
    public boolean validateTrainingSorting(String sortBy, String orderBy) {
        return (this.validateType(orderBy)) && Arrays.stream(TrainingSortByEnum.values()).anyMatch(e -> e.name().equals(sortBy));
    }

    @Override
    public boolean validateUserSorting(String sortBy, String orderBy) {
        return (this.validateType(orderBy)) && Arrays.stream(UserSortByEnum.values()).anyMatch(e -> e.name().equals(sortBy));
    }

    @Override
    public boolean validateTrainerSorting(String sortBy, String orderBy) {
        return (this.validateType(orderBy)) && Arrays.stream(TrainerSortByEnum.values()).anyMatch(e -> e.name().equals(sortBy));
    }

    private boolean validateType(String orderBy) {
        return orderBy.equals(ASCENDING_ORDER_NAME) || orderBy.equals(DESCENDING_ORDER_NAME);
    }

}
