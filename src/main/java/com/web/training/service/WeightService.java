package com.web.training.service;

import com.web.training.models.bindingModels.WeightBindingModel;
import com.web.training.models.serviceModels.WeightServiceModel;
import com.web.training.models.serviceModels.WeightStatisticServiceModel;

public interface WeightService {

    WeightServiceModel addWeightToUser(Long ownerId, WeightBindingModel weightDto);

    WeightStatisticServiceModel getWeightStatisticForUserId(Long userId);

    WeightServiceModel getCurrentWeightByUserId(Long userId);
}
