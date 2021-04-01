package com.web.training.service;


import com.web.training.models.exceptionModels.NotFoundException;
import com.web.training.models.entities.Day;
import com.web.training.models.entities.Training;
import com.web.training.models.enums.DayStatus;
import com.web.training.models.serviceModels.DayFullInfoServiceModel;
import com.web.training.repositories.DayRepository;
import com.web.training.repositories.WorkoutRepository;
import com.web.training.service.impl.DayServiceImpl;
import com.web.training.service.impl.WorkoutServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class DayServiceTests {

    @Mock
    private DayRepository dayRepository;
    @Mock
    private WorkoutRepository workoutRepository;

    private DayService dayService;
    private final ModelMapper modelMapper = new ModelMapper();


    private static final Long CORRECT_USER_ID = 2L;
    private static final Long CORRECT_DAY_ID = 1L;
    private static final Long CORRECT_TRAINING_ID = 1L;

    private static final Long WRONG_USER_ID = 1L;
    private static final Long WRONG_TRAINING_ID = 2L;
    private static final Long WRONG_DAY_ID = 2L;

    private Day testDay;

    @BeforeEach
    public void init() {
        this.testDay = new Day() {{
            setId(CORRECT_DAY_ID);
            setWorkouts(new HashSet<>());
            setDate(LocalDate.now());
            setStatus(DayStatus.InProgress);
            setTraining(new Training());
        }};
        Mockito.when(
                this.workoutRepository.countAllExercisesForDayId(this.testDay.getId())
        ).thenReturn(Optional.of(this.testDay.getWorkouts().size()));
        Mockito.when(
                this.workoutRepository.countAllByDayIdAndLinkIsNull(this.testDay.getId())
        ).thenReturn(Optional.of(this.testDay.getWorkouts().size()));
        Mockito.when(
                this.workoutRepository.countAllByDayIdAndLinkIsNotNull(this.testDay.getId())
        ).thenReturn(Optional.of(this.testDay.getWorkouts().size()));
        Mockito.when(
                this.dayRepository
                        .findByIdAndTrainingIdAndTraining_UserId(CORRECT_DAY_ID, CORRECT_TRAINING_ID, CORRECT_USER_ID)
        ).thenReturn(Optional.of(this.testDay));

        WorkoutService workoutService = new WorkoutServiceImpl(this.workoutRepository, null, this.modelMapper);
        this.dayService = new DayServiceImpl(this.dayRepository, workoutService, this.modelMapper);
    }

    /**
     * DayService - getFullInfoById(...) TESTS
     */

    @Test
    void getFullInfoByIdShouldReturnCorrectDayId() {
        DayFullInfoServiceModel actual = this.dayService.getFullInfoById(CORRECT_USER_ID, CORRECT_TRAINING_ID, CORRECT_DAY_ID);
        Assertions.assertEquals(this.testDay.getId(), actual.getId(), "Day is not correct");
    }

    @Test
    void getFullInfoByIdShouldReturnCorrectDayStatus() {
        DayFullInfoServiceModel actual = this.dayService.getFullInfoById(CORRECT_USER_ID, CORRECT_TRAINING_ID, CORRECT_DAY_ID);
        Assertions.assertEquals(this.testDay.getStatus(), actual.getStatus(), "Day is not correct");
    }

    @Test
    void getFullInfoByIdShouldReturnCorrectDayDate() {
        DayFullInfoServiceModel actual = this.dayService.getFullInfoById(CORRECT_USER_ID, CORRECT_TRAINING_ID, CORRECT_DAY_ID);
        Assertions.assertEquals(this.testDay.getDate(), actual.getDate(), "Day is not correct");
    }

    @Test
    void getFullInfoByIdShouldReturnCorrectDayWorkoutsSize() {
        DayFullInfoServiceModel actual = this.dayService.getFullInfoById(CORRECT_USER_ID, CORRECT_TRAINING_ID, CORRECT_DAY_ID);
        Assertions.assertEquals(this.testDay.getWorkouts().size(), actual.getWorkouts().size(), "Day is not correct");
    }

    @Test
    void getFullInfoByWrongUserIdShouldThrowException() {
        assertThrows(NotFoundException.class,
                () -> this.dayService.getFullInfoById(WRONG_USER_ID, CORRECT_TRAINING_ID, CORRECT_DAY_ID));
    }

    @Test
    void getFullInfoByWrongTrainingIdShouldThrowException() {
        assertThrows(NotFoundException.class,
                () -> this.dayService.getFullInfoById(CORRECT_USER_ID, WRONG_TRAINING_ID, CORRECT_DAY_ID));
    }

    @Test
    void getFullInfoByWrongDayIdShouldThrowException() {
        assertThrows(NotFoundException.class,
                () -> this.dayService.getFullInfoById(CORRECT_USER_ID, CORRECT_TRAINING_ID, WRONG_DAY_ID));
    }

    @Test
    void getFullInfoByWrongIdsShouldThrowException() {
        assertThrows(NotFoundException.class,
                () -> this.dayService.getFullInfoById(WRONG_USER_ID, WRONG_TRAINING_ID, WRONG_DAY_ID));
    }

    /**
     * DayService - changeDayStatusScheduled(...) TESTS
     */

}
