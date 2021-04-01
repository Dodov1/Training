package com.web.training.service.impl;

import com.web.training.models.exceptionModels.NotFoundException;
import com.web.training.models.entities.Workout;
import com.web.training.models.serviceModels.DayFullInfoServiceModel;
import com.web.training.models.serviceModels.ExerciseServiceModel;
import com.web.training.models.serviceModels.TodayServiceModel;
import com.web.training.models.serviceModels.WorkoutExercisesServiceModel;
import com.web.training.repositories.WorkoutRepository;
import com.web.training.service.UserService;
import com.web.training.service.WorkoutService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import static com.web.training.config.appConstants.AppConstants.USER_NOT_FOUND_EXCEPTION_MESSAGE;

@Service
public class WorkoutServiceImpl implements WorkoutService {

    private final WorkoutRepository workoutRepository;
    private final UserService userService;
    private final ModelMapper modelMapper;

    public WorkoutServiceImpl(WorkoutRepository workoutRepository, UserService userService, ModelMapper modelMapper) {
        this.workoutRepository = workoutRepository;
        this.userService = userService;
        this.modelMapper = modelMapper;
    }

    @Override
    public TodayServiceModel getAllWorkoutsForToday(LocalDate now, Long userId) {
        if (!this.userService.checkIfUserExistsById(userId)) {
            throw new NotFoundException(USER_NOT_FOUND_EXCEPTION_MESSAGE);
        }
        TodayServiceModel todayServiceModel = new TodayServiceModel();
        List<Workout> today = this.workoutRepository.getAllByDay_Training_UserIdAndDay_DateOrderById(userId, now);
        todayServiceModel.setWorkouts(today
                .stream()
                .map(e -> this.modelMapper.map(e, WorkoutExercisesServiceModel.class)
                        .setExercises(e.getExercises()
                                .stream()
                                .sorted((e1, e2) -> e2.getStatus().compareTo(e1.getStatus()))
                                .map(el -> this.modelMapper.map(el, ExerciseServiceModel.class))
                                .collect(Collectors.toList())
                        )
                )
                .collect(Collectors.toList()));
        todayServiceModel.setWorkoutsCount(
                this.workoutRepository.countAllByDay_DateAndLinkIsNullAndDay_Training_UserId(now, userId)
                        .orElse(0)
        );
        todayServiceModel.setWorkoutWithLinks(
                this.workoutRepository.sumLinkWorkouts(now, userId)
                        .orElse(0)
        );
        todayServiceModel.setWorkoutsExercisesCount(
                this.workoutRepository.sumExercises(now, userId)
                        .orElse(0)
        );
        return todayServiceModel;
    }

    @Override
    public DayFullInfoServiceModel getWorkoutsStatsForDay(DayFullInfoServiceModel model) {
        model.setWorkoutsCount(
                this.workoutRepository.countAllByDayIdAndLinkIsNull(model.getId())
                        .orElse(0)
        );
        model.setWorkoutsLinkCount(
                this.workoutRepository.countAllByDayIdAndLinkIsNotNull(model.getId())
                        .orElse(0)
        );
        model.setTotalExercises(
                this.workoutRepository.countAllExercisesForDayId(model.getId())
                        .orElse(0)
        );
        return model;
    }
}
