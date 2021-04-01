package com.web.training.service;

import com.web.training.models.exceptionModels.NotFoundException;
import com.web.training.models.bindingModels.ChangeStatusBindingModel;
import com.web.training.models.entities.Exercise;
import com.web.training.models.entities.Workout;
import com.web.training.models.enums.DurationType;
import com.web.training.models.enums.ExerciseStatusType;
import com.web.training.models.enums.ExerciseTarget;
import com.web.training.models.enums.ExerciseType;
import com.web.training.models.serviceModels.ExerciseServiceModel;
import com.web.training.repositories.ExerciseRepository;
import com.web.training.service.impl.ExerciseServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

@SpringBootTest
public class ExerciseServiceTests {

    @Mock
    private ExerciseRepository exerciseRepository;
    private ExerciseService exerciseService;
    private final ModelMapper modelMapper = new ModelMapper();

    private Exercise testExercise;
    private Workout testWorkout;
    private ChangeStatusBindingModel changeStatusBindingModel;

    private static final Long CORRECT_EXERCISE_ID = 1L;
    private static final Long WRONG_EXERCISE_ID = 2L;
    private static final Long CORRECT_WORKOUT_ID = 1L;
    private static final Long WRONG_WORKOUT_ID = 2L;
    private static final Long CORRECT_USER_ID = 1L;
    private static final Long WRONG_USER_ID = 2L;

    @BeforeEach
    public void init() {
        this.testWorkout = new Workout();
        this.testWorkout.setId(CORRECT_WORKOUT_ID);
        this.testExercise = new Exercise() {{
            setId(CORRECT_EXERCISE_ID);
            setDuration(2);
            setType(ExerciseType.Plate_Hops);
            setDurationType(DurationType.min);
            setStatus(ExerciseStatusType.Uncompleted);
            setTarget(ExerciseTarget.Cadence);
            setWorkout(testWorkout);
        }};
        Mockito.when(
                this.exerciseRepository.getByIdAndWorkoutIdAndWorkout_Day_Training_UserId(CORRECT_EXERCISE_ID, CORRECT_WORKOUT_ID, CORRECT_USER_ID)
        ).thenReturn(Optional.of(this.testExercise));
        this.changeStatusBindingModel = new ChangeStatusBindingModel();
        this.exerciseService = new ExerciseServiceImpl(this.exerciseRepository, this.modelMapper);
    }

    /**
     * ExerciseService - setDoneByExerciseId(...) TESTS
     */

    @Test
    void setDoneByExerciseWithCorrectIdsShouldReturnCompletedAsStatus() {
        this.changeStatusBindingModel.setStatusType(ExerciseStatusType.Completed);
        ExerciseServiceModel actual = this.exerciseService.setDoneByExerciseId(this.changeStatusBindingModel, CORRECT_EXERCISE_ID, CORRECT_WORKOUT_ID, CORRECT_USER_ID);
        Assertions.assertEquals(ExerciseStatusType.Completed, actual.getStatus(), "Exercise  is not correct");
    }

    @Test
    void setDoneByExerciseWithCorrectIdsShouldReturnUncompletedAsStatus() {
        this.testExercise.setStatus(ExerciseStatusType.Completed);
        this.changeStatusBindingModel.setStatusType(ExerciseStatusType.Uncompleted);
        ExerciseServiceModel actual = this.exerciseService.setDoneByExerciseId(this.changeStatusBindingModel, CORRECT_EXERCISE_ID, CORRECT_WORKOUT_ID, CORRECT_USER_ID);
        Assertions.assertEquals(ExerciseStatusType.Uncompleted, actual.getStatus(), "Exercise  is not correct");
    }

    @Test
    void setDoneByExerciseWithCorrectIdsShouldReturnCorrectDuration() {
        this.changeStatusBindingModel.setStatusType(ExerciseStatusType.Completed);
        ExerciseServiceModel actual = this.exerciseService.setDoneByExerciseId(this.changeStatusBindingModel, CORRECT_EXERCISE_ID, CORRECT_WORKOUT_ID, CORRECT_USER_ID);
        Assertions.assertEquals(this.testExercise.getDuration(), actual.getDuration(), "Exercise  is not correct");
    }

    @Test
    void setDoneByExerciseWithCorrectIdsShouldReturnCorrectDurationType() {
        this.changeStatusBindingModel.setStatusType(ExerciseStatusType.Completed);
        ExerciseServiceModel actual = this.exerciseService.setDoneByExerciseId(this.changeStatusBindingModel, CORRECT_EXERCISE_ID, CORRECT_WORKOUT_ID, CORRECT_USER_ID);
        Assertions.assertEquals(this.testExercise.getDurationType(), actual.getDurationType(), "Exercise  is not correct");
    }

    @Test
    void setDoneByExerciseWithCorrectIdsShouldReturnCorrectType() {
        this.changeStatusBindingModel.setStatusType(ExerciseStatusType.Completed);
        ExerciseServiceModel actual = this.exerciseService.setDoneByExerciseId(this.changeStatusBindingModel, CORRECT_EXERCISE_ID, CORRECT_WORKOUT_ID, CORRECT_USER_ID);
        Assertions.assertEquals(this.testExercise.getType(), actual.getType(), "Exercise  is not correct");
    }

    @Test
    void setDoneByExerciseWithCorrectIdsShouldReturnCorrectTarget() {
        this.changeStatusBindingModel.setStatusType(ExerciseStatusType.Completed);
        ExerciseServiceModel actual = this.exerciseService.setDoneByExerciseId(this.changeStatusBindingModel, CORRECT_EXERCISE_ID, CORRECT_WORKOUT_ID, CORRECT_USER_ID);
        Assertions.assertEquals(this.testExercise.getTarget(), actual.getTarget(), "Exercise  is not correct");
    }

    @Test
    void setDoneByExerciseWithCorrectIdsShouldReturnCorrectId() {
        this.changeStatusBindingModel.setStatusType(ExerciseStatusType.Completed);
        ExerciseServiceModel actual = this.exerciseService.setDoneByExerciseId(this.changeStatusBindingModel, CORRECT_EXERCISE_ID, CORRECT_WORKOUT_ID, CORRECT_USER_ID);
        Assertions.assertEquals(this.testExercise.getId(), actual.getId(), "Exercise  is not correct");
    }

    @Test
    void setDoneByExerciseWithWrongUserIdShouldReturnCorrectId() {
        this.changeStatusBindingModel.setStatusType(ExerciseStatusType.Completed);
        Assertions.assertThrows(NotFoundException.class, () ->
                this.exerciseService.setDoneByExerciseId(this.changeStatusBindingModel, CORRECT_EXERCISE_ID, CORRECT_WORKOUT_ID, WRONG_USER_ID)
        );
    }

    @Test
    void setDoneByExerciseWithWrongWorkoutIdShouldReturnCorrectId() {
        this.changeStatusBindingModel.setStatusType(ExerciseStatusType.Completed);
        Assertions.assertThrows(NotFoundException.class, () ->
                this.exerciseService.setDoneByExerciseId(this.changeStatusBindingModel, CORRECT_EXERCISE_ID, WRONG_WORKOUT_ID, CORRECT_USER_ID)
        );
    }

    @Test
    void setDoneByExerciseWithWrongExerciseIdShouldReturnCorrectId() {
        this.changeStatusBindingModel.setStatusType(ExerciseStatusType.Completed);
        Assertions.assertThrows(NotFoundException.class, () ->
                this.exerciseService.setDoneByExerciseId(this.changeStatusBindingModel, WRONG_EXERCISE_ID, CORRECT_WORKOUT_ID, CORRECT_USER_ID)
        );
    }

    @Test
    void setDoneByExerciseWithWrongIdsShouldReturnCorrectId() {
        this.changeStatusBindingModel.setStatusType(ExerciseStatusType.Completed);
        Assertions.assertThrows(NotFoundException.class, () ->
                this.exerciseService.setDoneByExerciseId(this.changeStatusBindingModel, WRONG_EXERCISE_ID, WRONG_WORKOUT_ID, WRONG_USER_ID)
        );
    }
}
