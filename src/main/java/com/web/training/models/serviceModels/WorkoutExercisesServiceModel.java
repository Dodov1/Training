package com.web.training.models.serviceModels;

import com.web.training.models.enums.WorkoutType;

import java.util.List;

public class WorkoutExercisesServiceModel {

    private Long id;
    private String name;
    private WorkoutType type;
    private String link;
    private List<ExerciseServiceModel> exercises;

    public WorkoutType getType() {
        return this.type;
    }

    public WorkoutExercisesServiceModel setType(WorkoutType type) {
        this.type = type;
        return this;
    }

    public String getLink() {
        return this.link;
    }

    public WorkoutExercisesServiceModel setLink(String link) {
        this.link = link;
        return this;
    }

    public Long getId() {
        return this.id;
    }

    public WorkoutExercisesServiceModel setId(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return this.name;
    }

    public WorkoutExercisesServiceModel setName(String name) {
        this.name = name;
        return this;
    }

    public List<ExerciseServiceModel> getExercises() {
        return this.exercises;
    }

    public WorkoutExercisesServiceModel setExercises(List<ExerciseServiceModel> exercises) {
        this.exercises = exercises;
        return this;
    }
}
