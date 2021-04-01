package com.web.training.service;

import com.web.training.models.exceptionModels.NoTrainerFoundForUserException;
import com.web.training.models.bindingModels.RatingBindingModel;
import com.web.training.models.bindingModels.RequestBindingModel;
import com.web.training.models.serviceModels.*;

import java.util.List;

public interface UserTrainerService {

    List<RequestServiceModel> getAllRequestsForTrainer(Long trainerId);

    List<RequestServiceModel> getAllRequestsForUser(Long userId);

    void requestManager(RequestBindingModel requestDto, Long trainerId, Long userId, boolean isTrainerRequester);

    UserProfileServiceModel getUserByIdForTrainer(Long trainerId, Long userId);

    UserProfileServiceModel getTrainerByIdForUser(Long userId, Long trainerId);

    List<UserTrainerInfoServiceModel> getUsersPageForTrainerId(Long trainerId, Integer page, Integer size, String sortBy, String orderBy);

    List<TrainerUserInfoServiceModel> getTrainersPageForUserId(Long userId, Integer page, Integer size, String sortBy, String orderBy);

    boolean checkIfTrainerHasUser(Long trainerId, Long userId);

    RatingServiceModel rateTrainer(Long userId, Long trainerId, RatingBindingModel ratingBindingModel) throws NoTrainerFoundForUserException;

    Double getRatingForTrainerId(Long id);

    Integer getTotalTrainerPagesCountForUserId(Long userId, Integer size);

    Integer getTotalUserPagesCountForTrainerId(Long trainerId, Integer size);

    Integer getUsersForTrainerCount(Long trainerId);
}
