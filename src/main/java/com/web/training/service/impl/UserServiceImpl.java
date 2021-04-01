package com.web.training.service.impl;

import com.web.training.config.events.EmailSenderEvent;
import com.web.training.models.exceptionModels.NotFoundException;
import com.web.training.models.exceptionModels.UserAlreadyExistsException;
import com.web.training.models.exceptionModels.UserIsAlreadyTrainerException;
import com.web.training.models.bindingModels.AddTrainerRequestBindingModel;
import com.web.training.models.bindingModels.SearchSuggestionBindingModel;
import com.web.training.models.bindingModels.UserRegisterBindingModel;
import com.web.training.models.entities.Authority;
import com.web.training.models.entities.Picture;
import com.web.training.models.entities.User;
import com.web.training.models.enums.TrainerType;
import com.web.training.models.security.UserDetailsModified;
import com.web.training.models.serviceModels.TrainerEnumsServiceModel;
import com.web.training.models.serviceModels.UserBasicPicServiceModel;
import com.web.training.models.serviceModels.UserProfileServiceModel;
import com.web.training.models.serviceModels.UserServiceModel;
import com.web.training.repositories.AuthorityRepository;
import com.web.training.repositories.PictureRepository;
import com.web.training.repositories.UserRepository;
import com.web.training.service.TrainerService;
import com.web.training.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.web.training.config.appConstants.AppConstants.*;

@Service
public class UserServiceImpl implements UserService, UserDetailsService {

    private final UserRepository userRepository;
    private final TrainerService trainerService;
    private final AuthorityRepository authorityRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;
    private final PictureRepository pictureRepository;
    private final ApplicationEventPublisher applicationEventPublisher;

    public UserServiceImpl(UserRepository userRepository, TrainerService trainerService, AuthorityRepository authorityRepository, PasswordEncoder passwordEncoder, ModelMapper modelMapper, PictureRepository pictureRepository, ApplicationEventPublisher applicationEventPublisher) {
        this.userRepository = userRepository;
        this.trainerService = trainerService;
        this.authorityRepository = authorityRepository;
        this.passwordEncoder = passwordEncoder;
        this.modelMapper = modelMapper;
        this.pictureRepository = pictureRepository;
        this.applicationEventPublisher = applicationEventPublisher;
    }


    @Override
    public UserDetailsModified loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> userEntityOpt = userRepository.findByUsername(username);
        return userEntityOpt
                .map(userEntity -> new UserDetailsModified(userEntity.getUsername(), userEntity.getPassword(),
                        userEntity.getAuthorities().stream().map(el -> new SimpleGrantedAuthority(el.getName())).collect(Collectors.toList()),
                        userEntity.getId(), userEntity.getTrainer(), userEntity.getEnabled())
                ).orElseThrow(() -> new UsernameNotFoundException("User " + username + " not found!"));
    }

    @Override
    public UserServiceModel getUserById(Long userId) {
        Optional<User> user = this.userRepository.findById(userId);
        return user.map(u -> this.modelMapper.map(u, UserServiceModel.class)
                .setTrainerStatus(u.getTrainer() == null ? null : u.getTrainer().getStatus()))
                .orElseThrow(() -> new NotFoundException(USER_NOT_FOUND_EXCEPTION_MESSAGE));
    }

    @Override
    public UserServiceModel addNewUser(UserRegisterBindingModel userRegisterBindingModel) {
        if (this.userRepository.findByUsername(userRegisterBindingModel.getUsername()).isPresent() ||
                this.userRepository.findByEmail(userRegisterBindingModel.getEmail()).isPresent()) {
            throw new UserAlreadyExistsException(USER_ALREADY_EXISTS);
        }
        User user = this.modelMapper.map(userRegisterBindingModel, User.class);
        user.setEnabled(false);
        Picture defaultPicture = this.pictureRepository.getByName(DEFAULT_PICTURE_NAME);
        user.setPassword(this.passwordEncoder.encode(userRegisterBindingModel.getPassword()));
        user.setPicture(defaultPicture);
        user.setTrainings(new HashSet<>());
        user.setWeights(new HashSet<>());
        user.setAuthorities(new ArrayList<>());
        user.setTrainers(new HashSet<>());
        user = this.userRepository.saveAndFlush(user);
        Authority authority = new Authority();
        authority.setUser(user);
        authority.setName(DEFAULT_USER_AUTHORITY);
        authority = this.authorityRepository.saveAndFlush(authority);
        user.getAuthorities().add(authority);

        EmailSenderEvent emailSenderEvent = new EmailSenderEvent(this, user);
        this.applicationEventPublisher.publishEvent(emailSenderEvent);

        return this.modelMapper.map(user, UserServiceModel.class);
    }

    @Override
    public List<UserBasicPicServiceModel> getSuggestionsByUsername(SearchSuggestionBindingModel model) {
        List<User> users;
        if (model.getNotIds().isEmpty()) {
            users = this.userRepository
                    .getTop5ByUsernameStartsWith(model.getInput());
        } else {
            users = this.userRepository
                    .getTop5ByUsernameStartsWithAndIdNotIn(model.getInput(), model.getNotIds());
        }
        return users
                .stream()
                .map(el -> this.modelMapper.map(el, UserBasicPicServiceModel.class)
                        .setProfilePicture(el.getPicture().getLocation()))
                .collect(Collectors.toList());
    }

    @Override
    public boolean requestToBecomeTrainer(Long userId, AddTrainerRequestBindingModel requestDto) throws UserIsAlreadyTrainerException {
        Optional<User> user = this.userRepository.findById(userId);
        if (user.isPresent()) {
            this.trainerService.addTrainer(user.get(), requestDto);
            return true;
        } else {
            throw new NotFoundException(USER_NOT_FOUND_EXCEPTION_MESSAGE);
        }
    }

    @Override
    public boolean enableUserById(Long userId) {
        return this.userRepository.findById(userId).map(el -> {
            el.setEnabled(USER_ENABLED);
            this.userRepository.saveAndFlush(el);
            return USER_ENABLED;
        }).orElseThrow(() -> new NotFoundException(USER_NOT_FOUND_EXCEPTION_MESSAGE));
    }

    @Override
    public boolean checkIfUsernameExists(String username) {
        return this.userRepository.findByUsername(username).isPresent();
    }

    @Override
    public boolean checkIfEmailExists(String email) {
        return this.userRepository.findByEmail(email).isPresent();
    }

    @Override
    public UserProfileServiceModel getUserProfileById(Long id) {
        return this.userRepository.findById(id)
                .map(el -> this.modelMapper.map(el, UserProfileServiceModel.class))
                .orElseThrow(() -> new NotFoundException(USER_NOT_FOUND_EXCEPTION_MESSAGE));
    }

    @Override
    public boolean checkIfUserExistsById(Long userId) {
        return this.userRepository.findById(userId).isPresent();
    }

    @Override
    public TrainerEnumsServiceModel getTrainerEnums() {
        return new TrainerEnumsServiceModel().setTrainerEnums(List.of(TrainerType.values()));
    }
}
