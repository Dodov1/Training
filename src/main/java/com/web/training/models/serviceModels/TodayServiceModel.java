package com.web.training.models.serviceModels;

import java.util.List;

public class TodayServiceModel {

    private Integer workoutsCount;
    private Integer workoutWithLinks;
    private Integer workoutsExercisesCount;
    private List<WorkoutExercisesServiceModel> workouts;

    public Integer getWorkoutWithLinks() {
        return this.workoutWithLinks;
    }

    public TodayServiceModel setWorkoutWithLinks(Integer workoutWithLinks) {
        this.workoutWithLinks = workoutWithLinks;
        return this;
    }

    public Integer getWorkoutsCount() {
        return this.workoutsCount;
    }

    public TodayServiceModel setWorkoutsCount(Integer workoutsCount) {
        this.workoutsCount = workoutsCount;
        return this;
    }

    public Integer getWorkoutsExercisesCount() {
        return this.workoutsExercisesCount;
    }

    public TodayServiceModel setWorkoutsExercisesCount(Integer workoutsExercisesCount) {
        this.workoutsExercisesCount = workoutsExercisesCount;
        return this;
    }

    public List<WorkoutExercisesServiceModel> getWorkouts() {
        return this.workouts;
    }

    public TodayServiceModel setWorkouts(List<WorkoutExercisesServiceModel> workouts) {
        this.workouts = workouts;
        return this;
    }
}
