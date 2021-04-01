package com.web.training.web;

import com.web.training.models.bindingModels.ChangeStatusBindingModel;
import com.web.training.models.serviceModels.ExerciseServiceModel;
import com.web.training.models.viewModels.ExerciseViewModel;
import com.web.training.service.ExerciseService;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class ExerciseController {

    private final ExerciseService exerciseService;
    private final ModelMapper modelMapper;

    public ExerciseController(ExerciseService exerciseService, ModelMapper modelMapper) {
        this.exerciseService = exerciseService;
        this.modelMapper = modelMapper;
    }

    @PatchMapping("/**/users/{userId}/workouts/{workoutId}/exercises/{exerciseId}")
    public ResponseEntity<ExerciseViewModel> setDoneByExerciseId(@Valid @RequestBody ChangeStatusBindingModel changeStatusBindingModel,
                                                                 @PathVariable("exerciseId") Long exerciseId,
                                                                 @PathVariable("workoutId") Long workoutId,
                                                                 @PathVariable("userId") Long userId) {
        ExerciseServiceModel result = this.exerciseService.setDoneByExerciseId(changeStatusBindingModel, exerciseId, workoutId,userId);
        return ResponseEntity.ok(this.modelMapper.map(result, ExerciseViewModel.class));
    }
}
