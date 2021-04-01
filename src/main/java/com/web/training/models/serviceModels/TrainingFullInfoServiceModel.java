package com.web.training.models.serviceModels;

import java.util.List;

public class TrainingFullInfoServiceModel {

    private TrainingServiceModel training;
    private List<DayFullInfoServiceModel> days;

    public TrainingServiceModel getTraining() {
        return this.training;
    }

    public TrainingFullInfoServiceModel setTraining(TrainingServiceModel training) {
        this.training = training;
        return this;
    }

    public List<DayFullInfoServiceModel> getDays() {
        return this.days;
    }

    public TrainingFullInfoServiceModel setDays(List<DayFullInfoServiceModel> days) {
        this.days = days;
        return this;
    }
}
