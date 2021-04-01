package com.web.training.service;

import com.web.training.models.bindingModels.TrainingEditBindingModel;
import com.web.training.models.serviceModels.ReadyTrainingServiceModel;
import com.web.training.models.serviceModels.TrainingBasicInfoServiceModel;
import com.web.training.models.serviceModels.TrainingFullInfoServiceModel;

import java.util.List;

public interface TrainerTrainingService {

    List<TrainingBasicInfoServiceModel> getReadyTrainings(Long trainerId);

    void addNewTrainingForTrainer(Long trainingId, Long trainerId);

    TrainingFullInfoServiceModel getReadyTrainingById(Long trainerId, Long trainingId);

    ReadyTrainingServiceModel editByTrainerAndTrainingById(Long trainerId, Long trainingId, TrainingEditBindingModel trainingEditBindingModel);
}
