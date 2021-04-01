package com.web.training.models.serviceModels;

import com.web.training.models.enums.DifficultyType;
import com.web.training.models.enums.TrainingType;

public class TrainingBasicInfoServiceModel {

    private Long id;
    private String title;
    private TrainingType trainingType;
    private DifficultyType difficulty;

    public TrainingType getTrainingType() {
        return this.trainingType;
    }

    public TrainingBasicInfoServiceModel setTrainingType(TrainingType trainingType) {
        this.trainingType = trainingType;
        return this;
    }

    public DifficultyType getDifficulty() {
        return this.difficulty;
    }

    public TrainingBasicInfoServiceModel setDifficulty(DifficultyType difficulty) {
        this.difficulty = difficulty;
        return this;
    }

    public Long getId() {
        return this.id;
    }

    public TrainingBasicInfoServiceModel setId(Long id) {
        this.id = id;
        return this;
    }

    public String getTitle() {
        return this.title;
    }

    public TrainingBasicInfoServiceModel setTitle(String title) {
        this.title = title;
        return this;
    }
}
