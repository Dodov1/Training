package com.web.training.models.viewModels;

import com.web.training.models.enums.DayStatus;
import com.web.training.models.serviceModels.WorkoutExercisesServiceModel;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class DayFullInfoViewModel {

    private Long id;
    private DayStatus status;
    private LocalDate date;
    private Integer workoutsCount;
    private Integer workoutsLinkCount;
    private Integer totalExercises;
    private List<WorkoutExercisesServiceModel> workouts;
}
