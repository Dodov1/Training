package com.web.training.models.serviceModels;

import java.util.List;

public class TrainingWithLinksServiceModel {

    private TrainingServiceModel training;
    private List<DayIdServiceModel> days;

    public TrainingServiceModel getTraining() {
        return this.training;
    }

    public TrainingWithLinksServiceModel setTraining(TrainingServiceModel training) {
        this.training = training;
        return this;
    }

    public List<DayIdServiceModel> getDays() {
        return this.days;
    }

    public TrainingWithLinksServiceModel setDays(List<DayIdServiceModel> days) {
        this.days = days;
        return this;
    }
}
