package com.web.training.service.impl;

import com.web.training.models.exceptionModels.NotFoundException;
import com.web.training.models.entities.Day;
import com.web.training.models.enums.DayStatus;
import com.web.training.models.serviceModels.DayFullInfoServiceModel;
import com.web.training.repositories.DayRepository;
import com.web.training.service.DayService;
import com.web.training.service.WorkoutService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

import static com.web.training.config.appConstants.AppConstants.NO_DAY_FOUND_WITH_ID;

@Service
public class DayServiceImpl implements DayService {

    private final DayRepository dayRepository;
    private final WorkoutService workoutService;
    private final ModelMapper modelMapper;

    public DayServiceImpl(DayRepository dayRepository, WorkoutService workoutService, ModelMapper modelMapper) {
        this.dayRepository = dayRepository;
        this.workoutService = workoutService;
        this.modelMapper = modelMapper;
    }

    @Override
    public void changeDayStatusScheduled() {
        this.dayRepository.updateDayStatusToDateInProgress(DayStatus.InProgress, LocalDate.now());
        this.dayRepository.updateDayStatusCompleted(DayStatus.Finished, LocalDate.now());
    }

    @Override
    public DayFullInfoServiceModel getFullInfoById(Long userId, Long trainingId, Long dayId) {
        Optional<Day> day = this.dayRepository.findByIdAndTrainingIdAndTraining_UserId(dayId, trainingId, userId);
        return day.map(value -> {
                    DayFullInfoServiceModel model = this.modelMapper.map(value, DayFullInfoServiceModel.class);
                    return workoutService.getWorkoutsStatsForDay(model);
                }
        ).orElseThrow(() -> new NotFoundException(String.format(NO_DAY_FOUND_WITH_ID, dayId)));
    }
}
