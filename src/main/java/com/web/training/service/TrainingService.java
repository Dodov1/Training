package com.web.training.service;

import com.web.training.models.bindingModels.SearchSuggestionBindingModel;
import com.web.training.models.bindingModels.TrainingAddBindingModel;
import com.web.training.models.bindingModels.TrainingEditBindingModel;
import com.web.training.models.serviceModels.*;

import java.time.LocalDate;
import java.util.List;

public interface TrainingService {

    TrainingsServiceModel getAllTrainingsForUserId(Long userId, Integer page, Integer size, String sortBy, String orderBy);

    TrainingStatisticServiceModel getTrainingsStatistics(Long userId);

    TrainingFullInfoServiceModel getFullTraining(Long userId, Long trainingId);

    TrainingWithLinksServiceModel getTrainingWithDayLinks(Long userId, Long trainingId);

    List<TrainingServiceModel> getSuggestionsByTitle(Long userId, SearchSuggestionBindingModel model);

    TrainingServiceModel editByTrainingById(Long ownerId, Long trainingId, TrainingEditBindingModel trainingDto);

    ReadyTrainingServiceModel editReadyTrainingById(Long trainingId, TrainingEditBindingModel trainingEditBindingModel);

    TrainingServiceModel addNewTraining(Long ownerId, TrainingAddBindingModel trainingEditBindingModel);

    ReadyTrainingServiceModel addNewReadyTraining(TrainingAddBindingModel trainingAddBindingModel);

    TrainingServiceModel addReadyTrainingToUser(Long trainingId, Long userId, LocalDate localDate);

    void setTrainingsStatusScheduled();

    TrainingEnumsServiceModel getTrainingEnums();
}
