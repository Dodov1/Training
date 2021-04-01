package com.web.training.models.serviceModels;

import com.web.training.models.enums.DifficultyType;
import com.web.training.models.enums.StatusType;
import com.web.training.models.enums.TrainingType;

import java.time.LocalDate;

public class TrainingServiceModel {

    private Long id;
    private LocalDate fromDate;
    private LocalDate toDate;
    private String title;
    private StatusType statusType;
    private String description;
    private TrainingType trainingType;
    private DifficultyType difficulty;

    public DifficultyType getDifficulty() {
        return this.difficulty;
    }

    public TrainingServiceModel setDifficulty(DifficultyType difficulty) {
        this.difficulty = difficulty;
        return this;
    }

    public StatusType getStatusType() {
        return this.statusType;
    }

    public TrainingServiceModel setStatusType(StatusType statusType) {
        this.statusType = statusType;
        return this;
    }

    public Long getId() {
        return this.id;
    }

    public TrainingServiceModel setId(Long id) {
        this.id = id;
        return this;
    }

    public String getTitle() {
        return this.title;
    }

    public TrainingServiceModel setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getDescription() {
        return this.description;
    }

    public TrainingServiceModel setDescription(String description) {
        this.description = description;
        return this;
    }

    public LocalDate getFromDate() {
        return this.fromDate;
    }

    public TrainingServiceModel setFromDate(LocalDate fromDate) {
        this.fromDate = fromDate;
        return this;
    }

    public LocalDate getToDate() {
        return this.toDate;
    }

    public TrainingServiceModel setToDate(LocalDate toDate) {
        this.toDate = toDate;
        return this;
    }

    public TrainingType getTrainingType() {
        return this.trainingType;
    }

    public TrainingServiceModel setTrainingType(TrainingType trainingType) {
        this.trainingType = trainingType;
        return this;
    }
}
