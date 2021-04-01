package com.web.training.models.bindingModels;

import com.web.training.models.enums.WorkoutType;
import lombok.Getter;
import lombok.Setter;
import net.bytebuddy.dynamic.TargetType;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Getter
@Setter
public class WorkoutEditBindingModel {

    @NotNull
    private Long id;

    @NotNull
    private String name;

    @NotNull
    private WorkoutType type;

    @NotNull
    private TargetType target;

    @NotEmpty
    private String link;

    @NotNull
    private Set<ExerciseEditBindingModel> exercises;
}
