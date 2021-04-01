package com.web.training.service.impl;

import com.web.training.models.exceptionModels.NotFoundException;
import com.web.training.models.bindingModels.ChangeStatusBindingModel;
import com.web.training.models.entities.Exercise;
import com.web.training.models.serviceModels.ExerciseServiceModel;
import com.web.training.repositories.ExerciseRepository;
import com.web.training.service.ExerciseService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.web.training.config.appConstants.AppConstants.NO_EXERCISE_WITH_ID;

@Service
public class ExerciseServiceImpl implements ExerciseService {

    private final ExerciseRepository exerciseRepository;
    private final ModelMapper modelMapper;

    public ExerciseServiceImpl(ExerciseRepository exerciseRepository, ModelMapper modelMapper) {
        this.exerciseRepository = exerciseRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public ExerciseServiceModel setDoneByExerciseId(ChangeStatusBindingModel changeStatusBindingModel, Long exerciseId, Long workoutId, Long userId) {
        Optional<Exercise> exercise = this.exerciseRepository.getByIdAndWorkoutIdAndWorkout_Day_Training_UserId(exerciseId, workoutId, userId);
        return exercise.map(el -> {
            el.setStatus(changeStatusBindingModel.getStatusType());
            this.exerciseRepository.saveAndFlush(el);
            return this.modelMapper.map(el, ExerciseServiceModel.class);
        }).orElseThrow(() -> new NotFoundException(String.format(NO_EXERCISE_WITH_ID, exerciseId)));
    }
}
