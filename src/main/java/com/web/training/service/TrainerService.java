package com.web.training.service;

import com.web.training.models.exceptionModels.NoUserFoundForTrainerException;
import com.web.training.models.exceptionModels.UserIsAlreadyTrainerException;
import com.web.training.models.bindingModels.*;
import com.web.training.models.entities.User;
import com.web.training.models.serviceModels.*;

import java.util.List;

public interface TrainerService {

    TrainerServiceModel getTrainerById(Long trainerId);

    List<TrainerServiceModel> getNotApprovedTrainers();

    boolean respondToTrainerRequest(Long trainerId, RespondTrainerRequestBindingModel requestDto);

    TrainerFullInfoServiceModel getTrainerInfoByUsername(String username);

    void addTrainer(User user, AddTrainerRequestBindingModel requestDto) throws UserIsAlreadyTrainerException;

    UserProfileServiceModel getTrainerProfileById(Long trainerId);

    List<UserBasicPicServiceModel> getSuggestionsByUsername(SearchSuggestionBindingModel searchSuggestionBindingModel);

    TrainingServiceModel addTrainingToUser(Long trainerId, Long userId, TrainingAddBindingModel trainingAddBindingModel) throws NoUserFoundForTrainerException;

    TrainingServiceModel editTrainingToUser(Long trainerId, Long userId, Long trainingId, TrainingEditBindingModel trainingEditBindingModel) throws NoUserFoundForTrainerException;

    ReadyTrainingServiceModel addReadyTraining(Long trainerId, TrainingAddBindingModel trainingAddBindingModel);

    TrainingServiceModel addReadyTrainingToUser(Long trainerId, Long userId, Long trainingId, ReadyTrainingStartBindingModel startDate) throws NoUserFoundForTrainerException;

    TrainingWithLinksServiceModel getTrainingWithDayLinks(Long trainerId, Long userId, Long trainingId) throws NoUserFoundForTrainerException;

    TrainingFullInfoServiceModel getFullTraining(Long trainerId, Long userId, Long trainingId) throws NoUserFoundForTrainerException;
}
