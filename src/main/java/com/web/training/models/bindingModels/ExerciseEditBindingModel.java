package com.web.training.models.bindingModels;

import com.web.training.models.enums.DurationType;
import com.web.training.models.enums.ExerciseTarget;
import com.web.training.models.enums.ExerciseType;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class ExerciseEditBindingModel {

    @NotNull
    private Long id;
    @NotNull
    @Min(1)
    private Integer duration;
    @NotNull
    private DurationType durationType;
    @NotNull
    private ExerciseTarget target;
    @NotNull
    private ExerciseType type;
}
