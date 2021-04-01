package com.web.training.models.serviceModels;

import java.util.ArrayList;
import java.util.List;

public class WeightStatisticServiceModel {

    private List<String> labels;
    private List<Double> data;
    private Double maxWeight;
    private Double minWeight;

    public WeightStatisticServiceModel() {
        this.labels = new ArrayList<>();
        this.data = new ArrayList<>();
    }

    public List<String> getLabels() {
        return this.labels;
    }

    public WeightStatisticServiceModel setLabels(List<String> labels) {
        this.labels = labels;
        return this;
    }

    public List<Double> getData() {
        return this.data;
    }

    public WeightStatisticServiceModel setData(List<Double> data) {
        this.data = data;
        return this;
    }

    public Double getMaxWeight() {
        return this.maxWeight;
    }

    public WeightStatisticServiceModel setMaxWeight(Double maxWeight) {
        this.maxWeight = maxWeight;
        return this;
    }

    public Double getMinWeight() {
        return this.minWeight;
    }

    public WeightStatisticServiceModel setMinWeight(Double minWeight) {
        this.minWeight = minWeight;
        return this;
    }
}
