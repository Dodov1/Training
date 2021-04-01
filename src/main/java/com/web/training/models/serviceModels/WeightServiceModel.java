package com.web.training.models.serviceModels;

import java.time.LocalDate;

public class WeightServiceModel {

    private Long id;
    private LocalDate date;
    private Double weight;
    private Double bmi;
    private String change;

    public Long getId() {
        return this.id;
    }

    public WeightServiceModel setId(Long id) {
        this.id = id;
        return this;
    }

    public String getChange() {
        return this.change;
    }

    public WeightServiceModel setChange(String change) {
        this.change = change;
        return this;
    }

    public Double getBmi() {
        return this.bmi;
    }

    public WeightServiceModel setBmi(Double bmi) {
        this.bmi = bmi;
        return this;
    }

    public LocalDate getDate() {
        return this.date;
    }

    public WeightServiceModel setDate(LocalDate date) {
        this.date = date;
        return this;
    }

    public Double getWeight() {
        return this.weight;
    }

    public WeightServiceModel setWeight(Double weight) {
        this.weight = weight;
        return this;
    }
}
