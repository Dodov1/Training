package com.web.training.service;

import com.web.training.models.bindingModels.ChangeStatusBindingModel;
import com.web.training.models.serviceModels.ExerciseServiceModel;

public interface ExerciseService {

    ExerciseServiceModel setDoneByExerciseId(ChangeStatusBindingModel changeStatusBindingModel, Long exerciseId, Long workoutId, Long userId);
}
