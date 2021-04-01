package com.web.training.models.bindingModels;

import com.web.training.models.enums.ExerciseStatusType;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class ChangeStatusBindingModel {

    @NotNull
    private ExerciseStatusType statusType;
}
