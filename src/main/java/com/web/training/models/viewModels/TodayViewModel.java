package com.web.training.models.viewModels;

import com.web.training.models.serviceModels.WorkoutExercisesServiceModel;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class TodayViewModel {

    private Integer workoutsCount;
    private Integer workoutWithLinks;
    private Integer workoutsExercisesCount;
    private List<WorkoutExercisesServiceModel> workouts;
}
