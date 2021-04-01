package com.web.training.service.impl;

import com.web.training.models.exceptionModels.*;
import com.web.training.models.bindingModels.RatingBindingModel;
import com.web.training.models.bindingModels.RequestBindingModel;
import com.web.training.models.entities.UserTrainer;
import com.web.training.models.enums.RelationStatus;
import com.web.training.models.enums.TrainerSortByEnum;
import com.web.training.models.enums.UserSortByEnum;
import com.web.training.models.serviceModels.*;
import com.web.training.repositories.TrainerRepository;
import com.web.training.repositories.UserRepository;
import com.web.training.repositories.UserTrainerRepository;
import com.web.training.service.UserTrainerService;
import com.web.training.validators.SortingValidator;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.web.training.config.appConstants.AppConstants.*;

@Service
public class UserTrainerServiceImpl implements UserTrainerService {

    private final UserTrainerRepository userTrainerRepository;
    private final UserRepository userRepository;
    private final TrainerRepository trainerRepository;
    private final SortingValidator sortingValidator;
    private final ModelMapper modelMapper;

    public UserTrainerServiceImpl(UserTrainerRepository userTrainerRepository, UserRepository userRepository, TrainerRepository trainerRepository, SortingValidator sortingValidator, ModelMapper modelMapper) {
        this.userTrainerRepository = userTrainerRepository;
        this.userRepository = userRepository;
        this.trainerRepository = trainerRepository;
        this.sortingValidator = sortingValidator;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<RequestServiceModel> getAllRequestsForTrainer(Long trainerId) {
        if (this.checkIfTrainerNotExists(trainerId)) {
            throw new TrainerNotFoundException(String.format(NO_TRAINER_WITH_ID, trainerId));
        }
        return this.userTrainerRepository
                .getAllByTrainerIdAndStatusTypeAndIsTrainerRequesterIsFalse(trainerId, RelationStatus.NotAnswered)
                .stream()
                .map(el -> this.modelMapper.map(el.getUser(), RequestServiceModel.class)
                        .setProfilePicture(el.getUser().getPicture().getLocation()))
                .collect(Collectors.toList());
    }

    @Override
    public List<RequestServiceModel> getAllRequestsForUser(Long userId) {
        if (this.checkIfUserNotExists(userId)) {
            throw new UserNotFoundException(USER_NOT_FOUND_EXCEPTION_MESSAGE);
        }
        return this.userTrainerRepository
                .getAllByUserIdAndStatusTypeAndIsTrainerRequesterIsTrue(userId, RelationStatus.NotAnswered)
                .stream()
                .map(el ->
                        this.modelMapper.map(el.getTrainer().getUser(), RequestServiceModel.class)
                                .setId(el.getTrainer().getId())
                                .setProfilePicture(el.getTrainer().getUser().getPicture().getLocation()))
                .collect(Collectors.toList());
    }


    @Override
    public void requestManager(RequestBindingModel requestDto, Long trainerId, Long userId, boolean isTrainerRequester) {
        Optional<UserTrainer> request = this.userTrainerRepository
                .getByTrainerIdAndUserId(
                        trainerId, userId
                );
        LocalDate localDate = LocalDate.now();
        if (request.isEmpty()) {
            request = Optional.of(new UserTrainer());
        }
        this.saveRequestFromManager(request.get(), isTrainerRequester, requestDto.getStatusType(), localDate, userId, trainerId);
    }

    @Override
    public UserProfileServiceModel getUserByIdForTrainer(Long trainerId, Long userId) {
        return this.userTrainerRepository.getByTrainerIdAndUserId(trainerId, userId)
                .map(el -> this.modelMapper.map(el.getUser(), UserProfileServiceModel.class)
                        .setStatusType(el.getStatusType()))
                .orElseThrow(() -> new UserNotFoundException(String.format(NO_USER_WITH_ID, userId)));
    }

    @Override
    public UserProfileServiceModel getTrainerByIdForUser(Long userId, Long trainerId) {
        return this.userTrainerRepository.getByTrainerIdAndUserId(trainerId, userId)
                .map(el -> this.modelMapper.map(el.getTrainer().getUser(), UserProfileServiceModel.class)
                        .setStatusType(el.getStatusType())
                        .setId(el.getTrainer().getId()))
                .orElseThrow(() -> new TrainerNotFoundException(String.format(NO_TRAINER_WITH_ID, trainerId)));
    }

    @Override
    public List<UserTrainerInfoServiceModel> getUsersPageForTrainerId(Long trainerId, Integer page, Integer size, String sortBy, String orderBy) {
        if (this.checkIfTrainerNotExists(trainerId)) {
            throw new NotFoundException(String.format(NO_TRAINER_WITH_ID, trainerId));
        }
        if (!this.sortingValidator.validateUserSorting(sortBy, orderBy)) {
            throw new InvalidSortingTypeException(INVALID_SORTING_ARGUMENTS);
        }
        List<UserTrainer> sorted = this.initDbRequest(page, size, sortBy, orderBy, trainerId, "user.");
        return sorted
                .stream()
                .map(el -> this.modelMapper.map(el.getUser(), UserTrainerInfoServiceModel.class)
                        .setTrainingCount(
                                this.userRepository.countAllTrainingsById(el.getUser().getId())
                                        .orElse(0)
                        )
                        .setProfilePicture(el.getUser().getPicture().getLocation())
                )
                .collect(Collectors.toList());
    }

    @Override
    public List<TrainerUserInfoServiceModel> getTrainersPageForUserId(Long userId, Integer page, Integer size, String sortBy, String orderBy) {
        if (this.checkIfUserNotExists(userId)) {
            throw new NotFoundException(USER_NOT_FOUND_EXCEPTION_MESSAGE);
        }
        if (!this.sortingValidator.validateTrainerSorting(sortBy, orderBy)) {
            throw new InvalidSortingTypeException(INVALID_SORTING_ARGUMENTS);
        }
        List<UserTrainer> sorted = this.initDbRequest(page, size, sortBy, orderBy, userId, "trainer.");
        return sorted
                .stream()
                .map(el -> this.modelMapper.map(el.getTrainer(), TrainerUserInfoServiceModel.class)
                        .setProfilePicture(el.getTrainer().getUser().getPicture().getLocation())
                        .setUsersCount(this.getUsersForTrainerCount(el.getTrainer().getId()))
                        .setUsername(el.getTrainer().getUser().getUsername())
                        .setRating(
                                this.userTrainerRepository.sumAvgRatingForTrainer(el.getTrainer().getId())
                                        .orElse(0.0))
                )
                .collect(Collectors.toList());
    }

    @Override
    public boolean checkIfTrainerHasUser(Long trainerId, Long userId) {
        return this.userTrainerRepository.getByTrainerIdAndUserIdAndStatusType(trainerId, userId, RelationStatus.Accepted).isPresent();
    }

    @Override
    public RatingServiceModel rateTrainer(Long userId, Long trainerId, RatingBindingModel ratingBindingModel) throws NoTrainerFoundForUserException {
        Optional<UserTrainer> userTrainer = this.userTrainerRepository.getByTrainerIdAndUserIdAndStatusType(trainerId, userId, RelationStatus.Accepted);

        return userTrainer
                .map(el -> {
                    el.setRating(ratingBindingModel.getRating());
                    this.userTrainerRepository.saveAndFlush(el);
                    Double rating = this.userTrainerRepository.sumAvgRatingForTrainer(trainerId)
                            .orElse(0.0);
                    return new RatingServiceModel().setRating(rating);
                })
                .orElseThrow(() -> new NoTrainerFoundForUserException(NO_USER_FOUND_FOR_TRAINER));
    }

    @Override
    public Double getRatingForTrainerId(Long trainerId) {
        return this.userTrainerRepository.sumAvgRatingForTrainer(trainerId).orElse(0.0);
    }

    @Override
    public Integer getTotalTrainerPagesCountForUserId(Long userId, Integer size) {
        int ost = this.userTrainerRepository.getAllByUserIdAndStatusType(userId, RelationStatus.Accepted).size() % size;
        int del = this.userTrainerRepository.getAllByUserIdAndStatusType(userId, RelationStatus.Accepted).size() / size;
        return ost == 0 && del != 0 ? del - 1 : del;
    }

    @Override
    public Integer getTotalUserPagesCountForTrainerId(Long trainerId, Integer size) {
        int del = this.userTrainerRepository.getAllByTrainerIdAndStatusType(trainerId, RelationStatus.Accepted).size() / size;
        int ost = this.userTrainerRepository.getAllByTrainerIdAndStatusType(trainerId, RelationStatus.Accepted).size() % size;
        return ost == 0 && del != 0 ? del - 1 : del;
    }

    @Override
    public Integer getUsersForTrainerCount(Long trainerId) {
        return this.userTrainerRepository.countAllByTrainerIdAndStatusType(trainerId, RelationStatus.Accepted);
    }

    private void saveRequestFromManager(UserTrainer request, boolean isTrainerRequester, RelationStatus statusType, LocalDate localDate, Long userId, Long trainerId) {
        request.setStatusType(statusType);
        request.setDate(localDate);
        request.setIsTrainerRequester(isTrainerRequester);
        request.setTrainer(
                this.trainerRepository.findById(trainerId)
                        .orElseThrow(() -> new TrainerNotFoundException(String.format(NO_TRAINER_WITH_ID, trainerId))));
        request.setUser(
                this.userRepository.findById(userId)
                        .orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND_EXCEPTION_MESSAGE)));
        this.userTrainerRepository.saveAndFlush(request);
    }

    private boolean checkIfUserNotExists(Long userId) {
        return this.userRepository.findById(userId).isEmpty();
    }

    private boolean checkIfTrainerNotExists(Long trainerId) {
        return this.trainerRepository.findById(trainerId).isEmpty();
    }

    private List<UserTrainer> initDbRequest(Integer page, Integer size, String sortBy, String orderBy, Long id, String prefix) {
        Sort sortingCriteria = null;
        long start = page * size.longValue();
        long endEl = (page * size.longValue()) + size;
        if (orderBy.equals(ASCENDING_ORDER_NAME)) {
            sortingCriteria = Sort.by(prefix + sortBy).ascending();
            if (sortBy.equals(UserSortByEnum.totalTrainings.name())) {
                return this.userTrainerRepository.getAllUsersTrainerIdSortedByTotalTrainingsAsc(id, start, endEl);
            }
            if (sortBy.equals(TrainerSortByEnum.totalUsers.name())) {
                return this.userTrainerRepository.getAllTrainersUserIdSortedByTotalUsersAsc(id, start, endEl);
            }
            if (sortBy.equals(TrainerSortByEnum.rating.name())) {
                sortingCriteria = Sort.by(sortBy).ascending();
            }
        } else if (orderBy.equals(DESCENDING_ORDER_NAME)) {
            sortingCriteria = Sort.by(prefix + sortBy).descending();
            if (sortBy.equals(UserSortByEnum.totalTrainings.name())) {
                return this.userTrainerRepository.getAllUsersTrainerIdSortedByTotalTrainingsDesc(id, start, endEl);
            }
            if (sortBy.equals(TrainerSortByEnum.totalUsers.name())) {
                return this.userTrainerRepository.getAllTrainersUserIdSortedByTotalUsersDesc(id, start, endEl);
            }
            if (sortBy.equals(TrainerSortByEnum.rating.name())) {
                sortingCriteria = Sort.by(sortBy).descending();
            }
        }
        Pageable pageRequest = PageRequest.of(page, size, sortingCriteria.and(Sort.by("id").ascending()));
        return prefix.equals("user.") ?
                this.userTrainerRepository.getAllByTrainerIdAndStatusType(id, RelationStatus.Accepted, pageRequest)
                : this.userTrainerRepository.getAllByUserIdAndStatusType(id, RelationStatus.Accepted, pageRequest);
    }
}
