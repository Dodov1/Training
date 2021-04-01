package com.web.training.models.serviceModels;

import com.web.training.models.enums.DayStatus;

import java.time.LocalDate;
import java.util.List;

public class DayFullInfoServiceModel {


    private Long id;
    private DayStatus status;
    private LocalDate date;
    private Integer workoutsCount;
    private Integer workoutsLinkCount;
    private Integer totalExercises;
    private List<WorkoutExercisesServiceModel> workouts;


    public Integer getWorkoutsCount() {
        return this.workoutsCount;
    }

    public DayFullInfoServiceModel setWorkoutsCount(Integer workoutsCount) {
        this.workoutsCount = workoutsCount;
        return this;
    }

    public Integer getWorkoutsLinkCount() {
        return this.workoutsLinkCount;
    }

    public DayFullInfoServiceModel setWorkoutsLinkCount(Integer workoutsLinkCount) {
        this.workoutsLinkCount = workoutsLinkCount;
        return this;
    }

    public Integer getTotalExercises() {
        return this.totalExercises;
    }

    public DayFullInfoServiceModel setTotalExercises(Integer totalExercises) {
        this.totalExercises = totalExercises;
        return this;
    }

    public List<WorkoutExercisesServiceModel> getWorkouts() {
        return this.workouts;
    }

    public DayFullInfoServiceModel setWorkouts(List<WorkoutExercisesServiceModel> workouts) {
        this.workouts = workouts;
        return this;
    }

    public Long getId() {
        return this.id;
    }

    public DayFullInfoServiceModel setId(Long id) {
        this.id = id;
        return this;
    }

    public DayStatus getStatus() {
        return this.status;
    }

    public DayFullInfoServiceModel setStatus(DayStatus status) {
        this.status = status;
        return this;
    }

    public LocalDate getDate() {
        return this.date;
    }

    public DayFullInfoServiceModel setDate(LocalDate date) {
        this.date = date;
        return this;
    }


}
