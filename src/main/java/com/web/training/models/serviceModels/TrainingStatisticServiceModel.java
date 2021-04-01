package com.web.training.models.serviceModels;

import java.util.ArrayList;
import java.util.List;

public class TrainingStatisticServiceModel {

    private List<String> labels;
    private List<Integer> data;

    public TrainingStatisticServiceModel() {
        this.labels = new ArrayList<>();
        this.data = new ArrayList<>();
    }

    public List<String> getLabels() {
        return this.labels;
    }

    public TrainingStatisticServiceModel setLabels(List<String> labels) {
        this.labels = labels;
        return this;
    }

    public List<Integer> getData() {
        return this.data;
    }

    public TrainingStatisticServiceModel setData(List<Integer> data) {
        this.data = data;
        return this;
    }
}
