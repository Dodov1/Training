package com.web.training.service.impl;

import com.web.training.models.exceptionModels.HasTheSameTrainingException;
import com.web.training.models.exceptionModels.InvalidSortingTypeException;
import com.web.training.models.exceptionModels.NotFoundException;
import com.web.training.models.bindingModels.*;
import com.web.training.models.entities.*;
import com.web.training.models.enums.*;
import com.web.training.models.serviceModels.*;
import com.web.training.repositories.*;
import com.web.training.service.TrainingService;
import com.web.training.validators.SortingValidator;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

import static com.web.training.config.appConstants.AppConstants.*;

@Service
public class TrainingServiceImpl implements TrainingService {

    private final TrainingRepository trainingRepository;
    private final DayRepository dayRepository;
    private final WorkoutRepository workoutRepository;
    private final ExerciseRepository exerciseRepository;
    private final UserRepository userRepository;
    private final SortingValidator sortingValidator;
    private final ModelMapper modelMapper;

    public TrainingServiceImpl(TrainingRepository trainingRepository, DayRepository dayRepository, WorkoutRepository workoutRepository, ExerciseRepository exerciseRepository, UserRepository userRepository, SortingValidator sortingValidator, ModelMapper modelMapper) {
        this.trainingRepository = trainingRepository;
        this.dayRepository = dayRepository;
        this.workoutRepository = workoutRepository;
        this.exerciseRepository = exerciseRepository;
        this.userRepository = userRepository;
        this.sortingValidator = sortingValidator;
        this.modelMapper = modelMapper;
    }

    @Override
    public ReadyTrainingServiceModel editReadyTrainingById(Long trainingId, TrainingEditBindingModel model) {
        Optional<Training> trainingEntity = this.trainingRepository.findById(trainingId);

        return trainingEntity.map(training -> {
                    deleteNotExistingDays(model, training);
                    training = this.modelMapper.map(model.getTraining(), Training.class);
                    StatusType statusType = null;
                    LocalDate currentDate = LocalDate.now();
                    if (currentDate.compareTo(model.getTraining().getFromDate()) >= 0 &&
                            currentDate.compareTo(model.getTraining().getToDate()) <= 0) {
                        statusType = StatusType.InProgress;
                    } else if (currentDate.compareTo(model.getTraining().getFromDate()) < 0) {
                        statusType = StatusType.Future;
                    } else {
                        statusType = StatusType.Completed;
                    }
                    training.setStatusType(statusType);
                    training = this.trainingRepository.saveAndFlush(training);
                    this.editAnyTraining(training, model.getDays());
                    return this.modelMapper.map(training, ReadyTrainingServiceModel.class);
                }
        ).orElseThrow(() -> new NotFoundException(String.format(NO_TRAINING_FOUND_WITH_ID, trainingId)));
    }

    @Override
    public TrainingServiceModel editByTrainingById(Long ownerId, Long trainingId, TrainingEditBindingModel model) {
        if (!this.checkIfUserExists(ownerId)) {
            throw new NotFoundException(USER_NOT_FOUND_EXCEPTION_MESSAGE);
        }
        Optional<Training> trainingEntity = this.trainingRepository.getByIdAndUserId(trainingId, ownerId);
        return trainingEntity.map(training -> {
                    User user = this.userRepository.findById(ownerId)
                            .orElseThrow(() -> new NotFoundException(USER_NOT_FOUND_EXCEPTION_MESSAGE));
                    deleteNotExistingDays(model, training);
                    training = this.modelMapper.map(model.getTraining(), Training.class);
                    LocalDate currentDate = LocalDate.now();
                    StatusType statusType = null;
                    if (currentDate.compareTo(model.getTraining().getFromDate()) >= 0 &&
                            currentDate.compareTo(model.getTraining().getToDate()) <= 0) {
                        statusType = StatusType.InProgress;
                    } else if (currentDate.compareTo(model.getTraining().getFromDate()) < 0) {
                        statusType = StatusType.Future;
                    } else {
                        statusType = StatusType.Completed;
                    }
                    training.setStatusType(statusType);
                    training.setUser(user);
                    training = this.trainingRepository.saveAndFlush(training);
                    this.editAnyTraining(training, model.getDays());
                    return this.modelMapper.map(training, TrainingServiceModel.class);
                }
        ).orElseThrow(() -> new NotFoundException(String.format(NO_TRAINING_FOUND_WITH_ID, trainingId)));
    }

    @Override
    public TrainingsServiceModel getAllTrainingsForUserId(Long userId, Integer page, Integer size, String sortBy, String orderBy) {
        if (!this.checkIfUserExists(userId)) {
            throw new NotFoundException(USER_NOT_FOUND_EXCEPTION_MESSAGE);
        }
        if (!this.sortingValidator.validateTrainingSorting(sortBy, orderBy)) {
            throw new InvalidSortingTypeException(INVALID_SORTING_ARGUMENTS);
        }
        Sort sortingCriteria = null;
        if (orderBy.equals(ASCENDING_ORDER_NAME)) {
            sortingCriteria = Sort.by(sortBy).ascending();
        } else if (orderBy.equals(DESCENDING_ORDER_NAME)) {
            sortingCriteria = Sort.by(sortBy).descending();
        }
        Pageable pageRequest = PageRequest.of(page, size, sortingCriteria.and(Sort.by("id").ascending()));
        List<Training> trainings = this.trainingRepository.findAllByUserId(userId, pageRequest);
        TrainingsServiceModel trainingsViewModel = new TrainingsServiceModel();

        int del = this.trainingRepository.getAllByUserId(userId).size() / size;
        int ost = this.trainingRepository.getAllByUserId(userId).size() % size;
        int totalPages = ost == 0 && del != 0 ? del - 1 : del;

        trainingsViewModel.setTotalPages(totalPages);
        if (trainings != null) {
            List<TrainingServiceModel> modelList = trainings.stream()
                    .map(el -> this.modelMapper.map(el, TrainingServiceModel.class))
                    .collect(Collectors.toList());
            trainingsViewModel.setTrainings(modelList);
            return trainingsViewModel;
        } else {
            return trainingsViewModel.setTrainings(new ArrayList<>());
        }
    }


    @Override
    public TrainingServiceModel addNewTraining(Long ownerId, TrainingAddBindingModel addModel) {
        User user = this.userRepository.findById(ownerId)
                .orElseThrow(() -> new NotFoundException(USER_NOT_FOUND_EXCEPTION_MESSAGE));
        int hasUserTheSameTraining = this.trainingRepository.countAllByUserIdAndTitleStartingWith(ownerId, addModel.getTitle()).orElse(0);
        if (hasUserTheSameTraining != 0) {
            throw new HasTheSameTrainingException(String.format(USER_HAS_ALREADY_TRAINING_WITH_NAME, addModel.getTitle()));
        }
        Training training = this.initTraining(addModel, user);
        return this.modelMapper.map(training, TrainingServiceModel.class);
    }

    @Override
    public TrainingWithLinksServiceModel getTrainingWithDayLinks(Long userId, Long trainingId) {
        if (!this.checkIfUserExists(userId)) {
            throw new NotFoundException(USER_NOT_FOUND_EXCEPTION_MESSAGE);
        }
        Optional<Training> training = this.trainingRepository.getByIdAndUserId(trainingId, userId);
        return training
                .map(el -> this.modelMapper.map(el, TrainingWithLinksServiceModel.class)
                        .setDays(el.getDays()
                                .stream()
                                .map(d -> this.modelMapper.map(d, DayIdServiceModel.class)
                                        .setWorkoutsCount(d.getWorkouts().size()))
                                .collect(Collectors.toList())))
                .orElseThrow(() -> new NotFoundException(String.format(NO_TRAINING_FOUND_WITH_ID, trainingId)));
    }

    @Override
    public TrainingFullInfoServiceModel getFullTraining(Long userId, Long trainingId) {
        if (!this.checkIfUserExists(userId)) {
            throw new NotFoundException(USER_NOT_FOUND_EXCEPTION_MESSAGE);
        }
        Optional<Training> training = this.trainingRepository.getByIdAndUserId(trainingId, userId);
        return training
                .map(el -> this.modelMapper.map(el, TrainingFullInfoServiceModel.class)
                        .setDays(el.getDays()
                                .stream()
                                .map(d -> this.modelMapper.map(d, DayFullInfoServiceModel.class)
                                        .setWorkoutsCount(d.getWorkouts().size()))
                                .collect(Collectors.toList()))
                )
                .orElseThrow(() -> new NotFoundException(String.format(NO_TRAINING_FOUND_WITH_ID, trainingId)));
    }

    @Override
    public void setTrainingsStatusScheduled() {
        this.trainingRepository.updateTrainingStatusFromDate(StatusType.InProgress, LocalDate.now());
        this.trainingRepository.updateTrainingStatusToDate(StatusType.Completed, LocalDate.now());
    }

    @Override
    public TrainingEnumsServiceModel getTrainingEnums() {
        TrainingEnumsServiceModel model = new TrainingEnumsServiceModel();
        model.setTrainingTypes(List.of(TrainingType.values()));
        model.setExerciseTypes(List.of(ExerciseType.values()));
        model.setExerciseTargets(List.of(ExerciseTarget.values()));
        model.setWorkoutTypes(List.of(WorkoutType.values()));
        model.setDurationTypes(List.of(DurationType.values()));
        model.setDifficultyTypes(List.of(DifficultyType.values()));
        return model;
    }

    @Override
    public ReadyTrainingServiceModel addNewReadyTraining(TrainingAddBindingModel trainingAddBindingModel) {
        Training training = this.initTraining(trainingAddBindingModel, null);
        this.trainingRepository.saveAndFlush(training);
        return this.modelMapper.map(training, ReadyTrainingServiceModel.class);
    }

    @Override
    public List<TrainingServiceModel> getSuggestionsByTitle(Long userId, SearchSuggestionBindingModel model) {
        if (!this.checkIfUserExists(userId)) {
            throw new NotFoundException(USER_NOT_FOUND_EXCEPTION_MESSAGE);
        }
        List<Training> trainings;
        if (model.getNotIds().isEmpty()) {
            trainings = this.trainingRepository.getTop5ByTitleStartsWithAndUser_Id(model.getInput(), userId);
        } else {
            trainings = this.trainingRepository.getTop5ByTitleStartsWithAndIdNotInAndUser_Id(model.getInput(), model.getNotIds(), userId);
        }
        if (trainings != null && !trainings.isEmpty()) {
            return trainings
                    .stream()
                    .map(el -> this.modelMapper.map(el, TrainingServiceModel.class))
                    .collect(Collectors.toList());
        } else {
            return new ArrayList<>();
        }
    }

    @Override
    public TrainingServiceModel addReadyTrainingToUser(Long trainingId, Long userId, LocalDate startDate) {
        return this.trainingRepository.findById(trainingId)
                .map(tr -> {
                    TrainingAddBindingModel map = this.modelMapper.map(tr, TrainingAddBindingModel.class);
                    Period period = Period.between(tr.getFromDate(), tr.getToDate());
                    int days = period.getDays();
                    map.setFromDate(startDate);
                    map.setToDate(startDate.plus(days, ChronoUnit.DAYS));
                    final int[] index = {0};
                    Set<DayAddBindingModel> collect = tr.getDays()
                            .stream()
                            .sorted(Comparator.comparing(Day::getDate))
                            .map(day -> {
                                day.setDate(startDate.plus(index[0]++, ChronoUnit.DAYS));
                                return this.modelMapper.map(day, DayAddBindingModel.class);
                            }).collect(Collectors.toSet());
                    map.setDays(collect);

                    return this.addNewTraining(userId, map);
                })
                .orElseThrow(() -> new NotFoundException(String.format(NO_TRAINING_FOUND_WITH_ID, trainingId)));
    }

    @Override
    public TrainingStatisticServiceModel getTrainingsStatistics(Long userId) {
        if (!this.checkIfUserExists(userId)) {
            throw new NotFoundException(USER_NOT_FOUND_EXCEPTION_MESSAGE);
        }
        TrainingStatisticServiceModel statistic = new TrainingStatisticServiceModel();

        int completedTrainings = this.trainingRepository.countAllByUserIdAndStatusType(userId, StatusType.Completed).orElse(0);
        statistic.getLabels().add(StatusType.Completed.name());
        statistic.getData().add(completedTrainings);

        int trainingsInProgress = this.trainingRepository.countAllByUserIdAndStatusType(userId, StatusType.InProgress).orElse(0);
        statistic.getLabels().add(StatusType.InProgress.name());
        statistic.getData().add(trainingsInProgress);

        int futureTrainings = this.trainingRepository.countAllByUserIdAndStatusType(userId, StatusType.Future).orElse(0);
        statistic.getLabels().add(StatusType.Future.name());
        statistic.getData().add(futureTrainings);

        return statistic;
    }

    private boolean checkIfUserExists(Long userId) {
        return this.userRepository.findById(userId).isPresent();
    }

    private void deleteNotExistingDays(TrainingEditBindingModel model, Training training) {
        if (training.getFromDate().compareTo(model.getTraining().getFromDate()) != 0 ||
                training.getToDate().compareTo(model.getTraining().getToDate()) != 0) {
            List<Long> ids = model.getDays().stream().map(DayEditBindingModel::getId).collect(Collectors.toList());
            if (ids.isEmpty()) {
                this.dayRepository.deleteAllByTrainingId(training.getId());
            } else {
                this.dayRepository.deleteAllByTrainingIdAndIdNotIn(training.getId(), ids);
            }
            training.setDays(new HashSet<>());
        }
    }

    private String extractLinkFromOriginal(String originalLink) {
        if (originalLink != null && originalLink.contains("=")
                && originalLink.contains(YOUTUBE_PREFIX_START)) {
            //https://www.youtube.com/watch?v = UZ2QwBgTgLo
            String value = originalLink.split("=")[1];
            return YOUTUBE_PREFIX + value;
        } else if (originalLink != null) {
            return originalLink;
        }
        return null;
    }

    private void editAnyTraining(Training training, List<DayEditBindingModel> days) {
        LocalDate currentDate = LocalDate.now();
        for (DayEditBindingModel dayDto : days) {
            Day day = this.modelMapper.map(dayDto, Day.class);
            if (dayDto.getId() != null) {
                List<Long> ids = dayDto.getWorkouts().stream().map(WorkoutEditBindingModel::getId).collect(Collectors.toList());
                if (ids.isEmpty()) {
                    this.workoutRepository.deleteAllByDayId(dayDto.getId());
                } else {
                    this.workoutRepository.deleteAllByDayIdAndIdNotIn(dayDto.getId(), ids);
                }
            }
            day.setWorkouts(new HashSet<>());

            DayStatus status = null;
            if (currentDate.compareTo(day.getDate()) == 0) {
                status = DayStatus.InProgress;
            } else if (currentDate.compareTo(day.getDate()) < 0) {
                status = DayStatus.Future;
            } else {
                status = DayStatus.Finished;
            }
            day.setStatus(status);
            day.setTraining(training);
            day = this.dayRepository.saveAndFlush(day);
            for (WorkoutEditBindingModel workoutDto : dayDto.getWorkouts()) {
                Workout workout = this.modelMapper.map(workoutDto, Workout.class);
                if (workoutDto.getId() != null) {
                    List<Long> ids = workoutDto.getExercises().stream().map(ExerciseEditBindingModel::getId).collect(Collectors.toList());
                    if (ids.isEmpty()) {
                        this.exerciseRepository.deleteAllByWorkoutId(workoutDto.getId());
                    } else {
                        this.exerciseRepository.deleteAllByWorkoutIdAndIdNotIn(workoutDto.getId(), ids);
                    }
                }
                workout.setExercises(new HashSet<>());

                workout.setDay(day);

                workout.setLink(this.extractLinkFromOriginal(workoutDto.getLink()));

                workout = this.workoutRepository.saveAndFlush(workout);
                if (workoutDto.getLink() == null) {
                    for (ExerciseEditBindingModel exerciseAddBindingModel : workoutDto.getExercises()) {
                        if (exerciseAddBindingModel.getId() == null) {
                            Exercise exercise = this.modelMapper.map(exerciseAddBindingModel, Exercise.class);
                            exercise.setStatus(ExerciseStatusType.Uncompleted);
                            exercise.setWorkout(workout);
                            this.exerciseRepository.saveAndFlush(exercise);
                        } else {
                            Optional<Exercise> entity = this.exerciseRepository.findById(exerciseAddBindingModel.getId());
                            if (entity.isPresent()) {
                                Exercise modified = this.modelMapper.map(exerciseAddBindingModel, Exercise.class);
                                modified.setStatus(entity.get().getStatus());
                                modified.setWorkout(workout);
                                this.exerciseRepository.saveAndFlush(modified);
                            } else {
                                throw new NotFoundException(String.format(NO_EXERCISE_WITH_ID, exerciseAddBindingModel.getId()));
                            }
                        }
                    }
                }
            }
        }
    }

    private Training initTraining(TrainingAddBindingModel trainingAddBindingModel, User user) {
        LocalDate currentDate = LocalDate.now();
        Training training = this.modelMapper.map(trainingAddBindingModel, Training.class);
        training.setDays(new HashSet<>());
        StatusType statusType = null;
        if (currentDate.compareTo(trainingAddBindingModel.getFromDate()) == 0) {
            statusType = StatusType.InProgress;
        } else if (currentDate.compareTo(trainingAddBindingModel.getFromDate()) < 0) {
            statusType = StatusType.Future;
        } else {
            statusType = StatusType.Completed;
        }
        training.setStatusType(statusType);
        training.setUser(user);
        training = this.trainingRepository.saveAndFlush(training);

        for (DayAddBindingModel dayDto : trainingAddBindingModel.getDays()) {
            Day day = this.modelMapper.map(dayDto, Day.class);
            day.setTraining(training);
            DayStatus status = null;
            if (currentDate.compareTo(day.getDate()) == 0) {
                status = DayStatus.InProgress;
            } else if (currentDate.compareTo(day.getDate()) < 0) {
                status = DayStatus.Future;
            }
            day.setStatus(status);
            day.setWorkouts(new HashSet<>());
            day = this.dayRepository.saveAndFlush(day);
            for (WorkoutBindingModel workoutDto : dayDto.getWorkouts()) {
                Workout workoutEntity = this.modelMapper.map(workoutDto, Workout.class);
                workoutEntity.setDay(day);
                workoutEntity.setExercises(new HashSet<>());

                workoutEntity.setLink(this.extractLinkFromOriginal(workoutDto.getLink()));

                workoutEntity = this.workoutRepository.saveAndFlush(workoutEntity);
                if (workoutDto.getLink() == null) {
                    for (ExerciseAddBindingModel exerciseAddBindingModel : workoutDto.getExercises()) {
                        Exercise exercise = this.modelMapper.map(exerciseAddBindingModel, Exercise.class);
                        exercise.setStatus(ExerciseStatusType.Uncompleted);
                        exercise.setWorkout(workoutEntity);

                        this.exerciseRepository.saveAndFlush(exercise);
                    }
                }
            }
        }
        return training;
    }


}
