package com.web.training.service.impl;

import com.web.training.models.exceptionModels.NoUserFoundForTrainerException;
import com.web.training.models.exceptionModels.NotFoundException;
import com.web.training.models.exceptionModels.UserIsAlreadyTrainerException;
import com.web.training.models.bindingModels.*;
import com.web.training.models.entities.Authority;
import com.web.training.models.entities.Trainer;
import com.web.training.models.entities.User;
import com.web.training.models.enums.RelationStatus;
import com.web.training.models.serviceModels.*;
import com.web.training.repositories.AuthorityRepository;
import com.web.training.repositories.TrainerRepository;
import com.web.training.service.TrainerService;
import com.web.training.service.TrainerTrainingService;
import com.web.training.service.TrainingService;
import com.web.training.service.UserTrainerService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.web.training.config.appConstants.AppConstants.*;

@Service
public class TrainerServiceImpl implements TrainerService {

    private final TrainerRepository trainerRepository;
    private final UserTrainerService userTrainerService;
    private final TrainerTrainingService trainerTrainingService;
    private final TrainingService trainingService;
    private final AuthorityRepository authorityRepository;
    private final ModelMapper modelMapper;

    public TrainerServiceImpl(TrainerRepository trainerRepository, UserTrainerService userTrainerService, TrainerTrainingService trainerTrainingService, TrainingService trainingService, AuthorityRepository authorityRepository, ModelMapper modelMapper) {
        this.trainerRepository = trainerRepository;
        this.userTrainerService = userTrainerService;
        this.trainerTrainingService = trainerTrainingService;
        this.trainingService = trainingService;
        this.authorityRepository = authorityRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public TrainerServiceModel getTrainerById(Long trainerId) {
        return this.trainerRepository.getByIdAndStatus(trainerId, RelationStatus.Accepted)
                .map(e -> this.modelMapper.map(e.getUser(), TrainerServiceModel.class).setId(e.getId()).setType(e.getType()))
                .orElseThrow(() -> new NotFoundException(String.format(NO_TRAINER_WITH_ID, trainerId)));
    }

    @Override
    public List<TrainerServiceModel> getNotApprovedTrainers() {
        List<Trainer> trainers = this.trainerRepository.getAllByStatus(RelationStatus.NotAnswered);
        return trainers
                .stream()
                .map(el -> this.modelMapper.map(el, TrainerServiceModel.class)
                        .setFirstName(el.getUser().getFirstName())
                        .setLastName(el.getUser().getLastName())
                        .setUsername(el.getUser().getUsername()))
                .collect(Collectors.toList());
    }

    @Override
    public boolean respondToTrainerRequest(Long trainerId, RespondTrainerRequestBindingModel requestDto) {
        Optional<Trainer> trainer = this.trainerRepository.getByIdAndStatus(trainerId, RelationStatus.NotAnswered);
        return trainer.map(el -> {
                    el.setStatus(requestDto.getRelationStatus());
                    this.trainerRepository.saveAndFlush(el);
                    Authority authority = new Authority();
                    authority.setUser(el.getUser());
                    authority.setName(DEFAULT_TRAINER_AUTHORITY);
                    authority = this.authorityRepository.saveAndFlush(authority);
                    el.getUser().getAuthorities().add(authority);

                    return true;
                }
        ).orElseThrow(() -> new NotFoundException(String.format(NO_TRAINER_WITH_ID, trainerId)));
    }

    @Override
    public TrainerFullInfoServiceModel getTrainerInfoByUsername(String username) {
        Optional<Trainer> trainer = this.trainerRepository.getByUser_UsernameAndStatus(username, RelationStatus.Accepted);
        return trainer.map(value -> this.modelMapper.map(value, TrainerFullInfoServiceModel.class)
                .setUsername(value.getUser().getUsername())
                .setFirstName(value.getUser().getFirstName())
                .setLastName(value.getUser().getLastName())
                .setTotalUsers(this.userTrainerService.getUsersForTrainerCount(value.getId()))
                .setRating(this.userTrainerService.getRatingForTrainerId(value.getId()))
        ).orElseThrow(() -> new NotFoundException(String.format(NO_TRAINER_WITH_USERNAME, username)));
    }

    @Override
    public void addTrainer(User user, AddTrainerRequestBindingModel requestDto) throws UserIsAlreadyTrainerException {
        Optional<Trainer> trainerDb = this.trainerRepository.getByUser(user);

        if (trainerDb.isPresent()) {
            if (trainerDb.get().getStatus().equals(RelationStatus.Accepted)) {
                throw new UserIsAlreadyTrainerException(USER_IS_ALREADY_TRAINER_EXCEPTION_MESSAGE);
            }
            trainerDb.get().setPhoneNumber(requestDto.getPhoneNumber());
            trainerDb.get().setType(requestDto.getTrainerType());
            trainerDb.get().setStatus(RelationStatus.NotAnswered);
            trainerDb.get().setFromDate(LocalDate.now());
            this.trainerRepository.saveAndFlush(trainerDb.get());
        } else {
            Trainer trainer = new Trainer(user, requestDto.getTrainerType(), RelationStatus.NotAnswered, LocalDate.now(), requestDto.getPhoneNumber());
            this.trainerRepository.saveAndFlush(trainer);
        }
    }

    @Override
    public UserProfileServiceModel getTrainerProfileById(Long trainerId) {
        return this.trainerRepository.getByIdAndStatus(trainerId, RelationStatus.Accepted)
                .map(el -> this.modelMapper.map(el.getUser(), UserProfileServiceModel.class).setId(el.getId()))
                .orElseThrow(() -> new NotFoundException(USER_NOT_FOUND_EXCEPTION_MESSAGE));
    }

    @Override
    public List<UserBasicPicServiceModel> getSuggestionsByUsername(SearchSuggestionBindingModel model) {
        List<Trainer> trainers;
        if (model.getNotIds().isEmpty()) {
            trainers = this.trainerRepository
                    .getTop5ByUser_UsernameContainingAndStatus(model.getInput(), RelationStatus.Accepted);
        } else {
            trainers = this.trainerRepository
                    .getTop5ByUser_UsernameContainingAndIdNotInAndStatus(model.getInput(), model.getNotIds(), RelationStatus.Accepted);
        }
        return trainers
                .stream()
                .map(el -> this.modelMapper.map(el.getUser(), UserBasicPicServiceModel.class)
                        .setProfilePicture(el.getUser().getPicture().getLocation())
                        .setId(el.getId()))
                .collect(Collectors.toList());
    }

    @Override
    public TrainingServiceModel addTrainingToUser(Long trainerId, Long userId, TrainingAddBindingModel trainingAddBindingModel) throws NoUserFoundForTrainerException {
        boolean hasUser = this.userTrainerService.checkIfTrainerHasUser(trainerId, userId);
        if (!hasUser) {
            throw new NoUserFoundForTrainerException(NO_USER_FOUND_FOR_TRAINER);
        }
        TrainingServiceModel newTraining = this.trainingService.addNewTraining(userId, trainingAddBindingModel);
        this.trainerTrainingService.addNewTrainingForTrainer(newTraining.getId(), trainerId);
        return newTraining;
    }

    @Override
    public TrainingServiceModel editTrainingToUser(Long trainerId, Long userId, Long trainingId, TrainingEditBindingModel trainingEditBindingModel) throws NoUserFoundForTrainerException {
        boolean hasUser = this.userTrainerService.checkIfTrainerHasUser(trainerId, userId);
        if (!hasUser) {
            throw new NoUserFoundForTrainerException(NO_USER_FOUND_FOR_TRAINER);
        }
        TrainingServiceModel newTraining = this.trainingService.editByTrainingById(userId, trainingId, trainingEditBindingModel);
        this.trainerTrainingService.addNewTrainingForTrainer(newTraining.getId(), trainerId);
        return newTraining;
    }

    @Override
    public ReadyTrainingServiceModel addReadyTraining(Long trainerId, TrainingAddBindingModel trainingModel) {
        return this.trainerRepository.getByIdAndStatus(trainerId, RelationStatus.Accepted)
                .map(el -> {
                    ReadyTrainingServiceModel model = this.trainingService.addNewReadyTraining(trainingModel);
                    this.trainerTrainingService.addNewTrainingForTrainer(model.getId(), trainerId);
                    return model;
                }).orElseThrow(() -> new NotFoundException(String.format(NO_TRAINER_WITH_ID, trainerId)));
    }

    @Override
    public TrainingServiceModel addReadyTrainingToUser(Long trainerId, Long userId, Long trainingId, ReadyTrainingStartBindingModel startDate) throws NoUserFoundForTrainerException {
        Optional<Trainer> trainer = this.trainerRepository.getByIdAndStatus(trainerId, RelationStatus.Accepted);
        if (trainer.isPresent()) {

            boolean hasUser = this.userTrainerService.checkIfTrainerHasUser(trainerId, userId);
            if (!hasUser) {
                throw new NoUserFoundForTrainerException(NO_USER_FOUND_FOR_TRAINER);
            }

            TrainingServiceModel model = this.trainingService.addReadyTrainingToUser(trainingId, userId, startDate.getStartDate());

            this.trainerTrainingService.addNewTrainingForTrainer(model.getId(), trainerId);

            return model;
        } else {
            throw new NotFoundException(String.format(NO_TRAINER_WITH_ID, trainerId));
        }
    }

    @Override
    public TrainingWithLinksServiceModel getTrainingWithDayLinks(Long trainerId, Long userId, Long trainingId) throws NoUserFoundForTrainerException {
        boolean hasUser = this.userTrainerService.checkIfTrainerHasUser(trainerId, userId);
        if (!hasUser) {
            throw new NoUserFoundForTrainerException(NO_USER_FOUND_FOR_TRAINER);
        }
        return this.trainingService.getTrainingWithDayLinks(userId, trainingId);
    }

    @Override
    public TrainingFullInfoServiceModel getFullTraining(Long trainerId, Long userId, Long trainingId) throws NoUserFoundForTrainerException {
        boolean hasUser = this.userTrainerService.checkIfTrainerHasUser(trainerId, userId);
        if (!hasUser) {
            throw new NoUserFoundForTrainerException(NO_USER_FOUND_FOR_TRAINER);
        }
        return this.trainingService.getFullTraining(userId, trainingId);
    }
}
