package com.web.training.models.bindingModels;

import com.web.training.models.enums.DayStatus;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Set;

@Getter
@Setter
public class DayEditBindingModel {

    @NotNull
    private Long id;
    @NotNull
    private DayStatus status;
    @NotNull
    private LocalDate date;
    @NotNull
    private Set<WorkoutEditBindingModel> workouts;
}
