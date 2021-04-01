package com.web.training.models.serviceModels;

import com.web.training.models.enums.*;

import java.util.List;

public class TrainingEnumsServiceModel {

    private List<TrainingType> trainingTypes;
    private List<ExerciseType> exerciseTypes;
    private List<ExerciseTarget> exerciseTargets;
    private List<WorkoutType> workoutTypes;
    private List<DurationType> durationTypes;
    private List<DifficultyType> difficultyTypes;

    public List<TrainingType> getTrainingTypes() {
        return this.trainingTypes;
    }

    public TrainingEnumsServiceModel setTrainingTypes(List<TrainingType> trainingTypes) {
        this.trainingTypes = trainingTypes;
        return this;
    }

    public List<ExerciseType> getExerciseTypes() {
        return this.exerciseTypes;
    }

    public TrainingEnumsServiceModel setExerciseTypes(List<ExerciseType> exerciseTypes) {
        this.exerciseTypes = exerciseTypes;
        return this;
    }

    public List<ExerciseTarget> getExerciseTargets() {
        return this.exerciseTargets;
    }

    public TrainingEnumsServiceModel setExerciseTargets(List<ExerciseTarget> exerciseTargets) {
        this.exerciseTargets = exerciseTargets;
        return this;
    }

    public List<WorkoutType> getWorkoutTypes() {
        return this.workoutTypes;
    }

    public TrainingEnumsServiceModel setWorkoutTypes(List<WorkoutType> workoutTypes) {
        this.workoutTypes = workoutTypes;
        return this;
    }

    public List<DurationType> getDurationTypes() {
        return this.durationTypes;
    }

    public TrainingEnumsServiceModel setDurationTypes(List<DurationType> durationTypes) {
        this.durationTypes = durationTypes;
        return this;
    }

    public List<DifficultyType> getDifficultyTypes() {
        return this.difficultyTypes;
    }

    public TrainingEnumsServiceModel setDifficultyTypes(List<DifficultyType> difficultyTypes) {
        this.difficultyTypes = difficultyTypes;
        return this;
    }
}
