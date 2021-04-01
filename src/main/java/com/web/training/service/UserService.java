package com.web.training.service;

import com.web.training.models.exceptionModels.UserIsAlreadyTrainerException;
import com.web.training.models.bindingModels.*;
import com.web.training.models.serviceModels.*;

import java.util.List;

public interface UserService {

    UserServiceModel getUserById(Long userId);

    List<UserBasicPicServiceModel> getSuggestionsByUsername(SearchSuggestionBindingModel getSuggestionsByUsername);

    boolean requestToBecomeTrainer(Long userId, AddTrainerRequestBindingModel requestDto) throws UserIsAlreadyTrainerException;

    UserServiceModel addNewUser(UserRegisterBindingModel userRegisterBindingModel);

    boolean enableUserById(Long userId);

    boolean checkIfUsernameExists(String username);

    boolean checkIfEmailExists(String email);

    UserProfileServiceModel getUserProfileById(Long id);

    boolean checkIfUserExistsById(Long userId);

    TrainerEnumsServiceModel getTrainerEnums();
}
