package com.web.training.models.viewModels;

import com.web.training.models.enums.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class TrainingEnumsViewModel {

    private List<TrainingType> trainingTypes;
    private List<ExerciseType> exerciseTypes;
    private List<ExerciseTarget> exerciseTargets;
    private List<WorkoutType> workoutTypes;
    private List<DurationType> durationTypes;
    private List<DifficultyType> difficultyTypes;
}
