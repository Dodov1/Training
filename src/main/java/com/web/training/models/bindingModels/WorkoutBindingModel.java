package com.web.training.models.bindingModels;

import com.web.training.models.enums.WorkoutType;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Getter
@Setter
public class WorkoutBindingModel {

    @NotNull
    private String name;

    @NotNull
    private WorkoutType type;

    @NotEmpty
    private String link;

    @NotNull
    private Set<ExerciseAddBindingModel> exercises;
}
