package com.web.training.service;

import com.web.training.models.serviceModels.DayFullInfoServiceModel;

public interface DayService {

    void changeDayStatusScheduled();

    DayFullInfoServiceModel getFullInfoById(Long userId, Long trainingId, Long dayId);
}
