package com.web.training.service;

import com.web.training.models.serviceModels.DayFullInfoServiceModel;
import com.web.training.models.serviceModels.TodayServiceModel;

import java.time.LocalDate;

public interface WorkoutService {

    TodayServiceModel getAllWorkoutsForToday(LocalDate now, Long userId);

    DayFullInfoServiceModel getWorkoutsStatsForDay(DayFullInfoServiceModel model);
}
