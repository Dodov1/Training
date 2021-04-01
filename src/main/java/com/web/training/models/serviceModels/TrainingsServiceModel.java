package com.web.training.models.serviceModels;

import java.util.List;

public class TrainingsServiceModel {

    private List<TrainingServiceModel> trainings;
    private Integer totalPages;

    public List<TrainingServiceModel> getTrainings() {
        return this.trainings;
    }

    public TrainingsServiceModel setTrainings(List<TrainingServiceModel> trainings) {
        this.trainings = trainings;
        return this;
    }

    public Integer getTotalPages() {
        return this.totalPages;
    }

    public TrainingsServiceModel setTotalPages(Integer totalPages) {
        this.totalPages = totalPages;
        return this;
    }
}
