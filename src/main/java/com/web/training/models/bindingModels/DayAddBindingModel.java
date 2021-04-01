package com.web.training.models.bindingModels;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Set;

@Getter
@Setter
public class DayAddBindingModel {

    @NotNull
    private LocalDate date;
    @NotNull
    private Set<WorkoutBindingModel> workouts;

}
