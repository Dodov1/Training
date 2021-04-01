package com.web.training.models.serviceModels;

import java.time.LocalDate;

public class DayIdServiceModel {

    private Long id;
    private LocalDate date;
    private Integer workoutsCount;

    public Integer getWorkoutsCount() {
        return this.workoutsCount;
    }

    public DayIdServiceModel setWorkoutsCount(Integer workoutsCount) {
        this.workoutsCount = workoutsCount;
        return this;
    }

    public Long getId() {
        return this.id;
    }

    public DayIdServiceModel setId(Long id) {
        this.id = id;
        return this;
    }

    public LocalDate getDate() {
        return this.date;
    }

    public DayIdServiceModel setDate(LocalDate date) {
        this.date = date;
        return this;
    }
}
