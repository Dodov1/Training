package com.web.training.models.serviceModels;

import com.web.training.models.enums.DifficultyType;
import com.web.training.models.enums.StatusType;
import com.web.training.models.enums.TrainingType;

public class ReadyTrainingServiceModel {

    private Long id;
    private String title;
    private StatusType statusType;
    private String description;
    private TrainingType trainingType;
    private DifficultyType difficulty;

    public Long getId() {
        return this.id;
    }

    public ReadyTrainingServiceModel setId(Long id) {
        this.id = id;
        return this;
    }

    public String getTitle() {
        return this.title;
    }

    public ReadyTrainingServiceModel setTitle(String title) {
        this.title = title;
        return this;
    }

    public StatusType getStatusType() {
        return this.statusType;
    }

    public ReadyTrainingServiceModel setStatusType(StatusType statusType) {
        this.statusType = statusType;
        return this;
    }

    public String getDescription() {
        return this.description;
    }

    public ReadyTrainingServiceModel setDescription(String description) {
        this.description = description;
        return this;
    }

    public TrainingType getTrainingType() {
        return this.trainingType;
    }

    public ReadyTrainingServiceModel setTrainingType(TrainingType trainingType) {
        this.trainingType = trainingType;
        return this;
    }

    public DifficultyType getDifficulty() {
        return this.difficulty;
    }

    public ReadyTrainingServiceModel setDifficulty(DifficultyType difficulty) {
        this.difficulty = difficulty;
        return this;
    }
}
