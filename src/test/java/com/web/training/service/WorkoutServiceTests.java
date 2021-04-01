package com.web.training.service;

import com.web.training.models.exceptionModels.NotFoundException;
import com.web.training.models.entities.Day;
import com.web.training.models.entities.Exercise;
import com.web.training.models.entities.Workout;
import com.web.training.models.enums.*;
import com.web.training.models.serviceModels.DayFullInfoServiceModel;
import com.web.training.models.serviceModels.TodayServiceModel;
import com.web.training.models.serviceModels.WorkoutExercisesServiceModel;
import com.web.training.repositories.WorkoutRepository;
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
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class WorkoutServiceTests {

    @Mock
    private WorkoutRepository workoutRepository;
    @Mock
    private UserService userService;
    private WorkoutService workoutService;
    private final ModelMapper modelMapper = new ModelMapper();

    private Workout testLinkWorkout;
    private Workout testCustomWorkout;
    private Day testDay;
    private Exercise testExercise2;

    private static final Long CORRECT_LINK_WORKOUT_ID = 1L;
    private static final Long CORRECT_CUSTOM_WORKOUT_ID = 2L;
    private static final String CORRECT_LINK_WORKOUT_NAME = "Link Workout";
    private static final String CORRECT_CUSTOM_WORKOUT_NAME = "Custom Workout";
    private static final LocalDate CORRECT_LOCAL_DATE = LocalDate.now();
    private static final Long CORRECT_USER_ID = 1L;
    private static final Long WRONG_USER_ID = 2L;

    @BeforeEach
    public void init() {
        this.testDay = new Day();
        this.testDay.setId(1L);

        this.testLinkWorkout = new Workout() {{
            setId(CORRECT_LINK_WORKOUT_ID);
            setLink("https://demoLink.com");
            setType(WorkoutType.Link);
            setName(CORRECT_LINK_WORKOUT_NAME);
            setDay(testDay);
            setExercises(new HashSet<>());
        }};
        this.testCustomWorkout = new Workout() {{
            setId(CORRECT_CUSTOM_WORKOUT_ID);
            setName(CORRECT_CUSTOM_WORKOUT_NAME);
            setType(WorkoutType.Custom);
            setDay(testDay);
            setExercises(new HashSet<>());
        }};
        Exercise testExercise = new Exercise() {{
            setId(2L);
            setDuration(2);
            setType(ExerciseType.Plate_Hops);
            setDurationType(DurationType.min);
            setStatus(ExerciseStatusType.Uncompleted);
            setTarget(ExerciseTarget.Cadence);
            setWorkout(testCustomWorkout);
        }};
        this.testExercise2 = new Exercise() {{
            setId(1L);
            setDuration(2);
            setType(ExerciseType.Tuck_Jumps);
            setDurationType(DurationType.km);
            setStatus(ExerciseStatusType.Completed);
            setTarget(ExerciseTarget.Pace);
            setWorkout(testCustomWorkout);
        }};
        this.testCustomWorkout.getExercises().add(this.testExercise2);
        this.testCustomWorkout.getExercises().add(testExercise);
        this.testDay.setWorkouts(new HashSet<>());
        this.testDay.getWorkouts().add(this.testLinkWorkout);
        this.testDay.getWorkouts().add(this.testCustomWorkout);
        Mockito.when(this.userService
                .checkIfUserExistsById(CORRECT_USER_ID))
                .thenReturn(true);
        Mockito.when(
                this.workoutRepository.getAllByDay_Training_UserIdAndDay_DateOrderById(CORRECT_USER_ID, CORRECT_LOCAL_DATE)
        ).thenReturn(List.of(this.testLinkWorkout, this.testCustomWorkout));
        this.workoutService = new WorkoutServiceImpl(this.workoutRepository, userService, this.modelMapper);
    }

    /**
     * WorkoutService - getAllWorkoutsForToday(...) TESTS
     */

    @Test
    void getTodayShouldReturnCorrectWorkoutsCountWithoutLink() {
        Mockito.when(
                this.workoutRepository.countAllByDay_DateAndLinkIsNullAndDay_Training_UserId(CORRECT_LOCAL_DATE, CORRECT_USER_ID))
                .thenReturn(Optional.of(Integer.valueOf("1")));
        TodayServiceModel actual = this.workoutService.getAllWorkoutsForToday(CORRECT_LOCAL_DATE, CORRECT_USER_ID);
        Assertions.assertEquals(1, actual.getWorkoutsCount(), "Workout  is not correct");
    }

    @Test
    void getTodayShouldReturnCorrectWorkoutsCountWithLink() {
        Mockito.when(
                this.workoutRepository.sumLinkWorkouts(CORRECT_LOCAL_DATE, CORRECT_USER_ID))
                .thenReturn(Optional.of(Integer.valueOf("1")));
        TodayServiceModel actual = this.workoutService.getAllWorkoutsForToday(CORRECT_LOCAL_DATE, CORRECT_USER_ID);
        Assertions.assertEquals(1, actual.getWorkoutWithLinks(), "Workout  is not correct");
    }

    @Test
    void getTodayShouldReturnCorrectExercisesCount() {
        Mockito.when(
                this.workoutRepository.sumExercises(CORRECT_LOCAL_DATE, CORRECT_USER_ID))
                .thenReturn(Optional.of(Integer.valueOf("1")));
        TodayServiceModel actual = this.workoutService.getAllWorkoutsForToday(CORRECT_LOCAL_DATE, CORRECT_USER_ID);
        Assertions.assertEquals(1, actual.getWorkoutsExercisesCount(), "Workout  is not correct");
    }

    @Test
    void getTodayWithZeroWorkoutsShouldReturnZeroWorkoutsCountWithoutLink() {
        Mockito.when(
                this.workoutRepository.countAllByDay_DateAndLinkIsNullAndDay_Training_UserId(CORRECT_LOCAL_DATE, CORRECT_USER_ID))
                .thenReturn(Optional.empty());
        TodayServiceModel actual = this.workoutService.getAllWorkoutsForToday(CORRECT_LOCAL_DATE, CORRECT_USER_ID);
        Assertions.assertEquals(0, actual.getWorkoutsCount(), "Workout  is not correct");
    }

    @Test
    void getTodayWithZeroWorkoutsShouldReturnZeroWorkoutsCountWithLink() {
        Mockito.when(
                this.workoutRepository.sumLinkWorkouts(CORRECT_LOCAL_DATE, CORRECT_USER_ID))
                .thenReturn(Optional.empty());
        TodayServiceModel actual = this.workoutService.getAllWorkoutsForToday(CORRECT_LOCAL_DATE, CORRECT_USER_ID);
        Assertions.assertEquals(0, actual.getWorkoutWithLinks(), "Workout  is not correct");
    }

    @Test
    void getTodayWithZeroWorkoutsShouldReturnZeroExercisesCount() {
        Mockito.when(
                this.workoutRepository.sumExercises(CORRECT_LOCAL_DATE, CORRECT_USER_ID))
                .thenReturn(Optional.empty());
        TodayServiceModel actual = this.workoutService.getAllWorkoutsForToday(CORRECT_LOCAL_DATE, CORRECT_USER_ID);
        Assertions.assertEquals(0, actual.getWorkoutsExercisesCount(), "Workout  is not correct");
    }

    @Test
    void getTodayShouldReturnCorrectWorkoutsCount() {
        TodayServiceModel actual = this.workoutService.getAllWorkoutsForToday(CORRECT_LOCAL_DATE, CORRECT_USER_ID);
        Assertions.assertEquals(2, actual.getWorkouts().size(), "Workout  is not correct");
    }

    @Test
    void getTodayShouldReturnCorrectSortedWorkouts() {
        TodayServiceModel actual = this.workoutService.getAllWorkoutsForToday(CORRECT_LOCAL_DATE, CORRECT_USER_ID);
        Assertions.assertEquals(List.of(this.testLinkWorkout, this.testCustomWorkout)
                        .stream()
                        .map(el -> this.modelMapper.map(el, WorkoutExercisesServiceModel.class))
                        .collect(Collectors.toList()).get(0).getId()
                , actual.getWorkouts().get(0).getId(), "Workout  is not correct");
    }

    @Test
    void getTodayShouldReturnCorrectSortedWorkouts2() {
        Mockito.when(
                this.workoutRepository.sumExercises(CORRECT_LOCAL_DATE, CORRECT_USER_ID))
                .thenReturn(Optional.empty());
        TodayServiceModel actual = this.workoutService.getAllWorkoutsForToday(CORRECT_LOCAL_DATE, CORRECT_USER_ID);
        Assertions.assertEquals(List.of(this.testLinkWorkout, this.testCustomWorkout)
                        .stream()
                        .map(el -> this.modelMapper.map(el, WorkoutExercisesServiceModel.class))
                        .collect(Collectors.toList()).get(1).getId()
                , actual.getWorkouts().get(1).getId(), "Workout  is not correct");
    }

    @Test
    void getTodayShouldReturnCorrectSortedWorkouts3() {
        TodayServiceModel actual = this.workoutService.getAllWorkoutsForToday(CORRECT_LOCAL_DATE, CORRECT_USER_ID);
        Assertions.assertNotEquals(List.of(this.testLinkWorkout, this.testCustomWorkout)
                        .stream()
                        .map(el -> this.modelMapper.map(el, WorkoutExercisesServiceModel.class))
                        .collect(Collectors.toList()).get(0).getId()
                , actual.getWorkouts().get(1).getId(), "Workout  is not correct");
    }

    @Test
    void getTodayShouldReturnZeroSortedExercisesWithWithLink() {
        TodayServiceModel actual = this.workoutService.getAllWorkoutsForToday(CORRECT_LOCAL_DATE, CORRECT_USER_ID);
        Assertions.assertEquals(0, actual.getWorkouts().get(0).getExercises().size(), "Workout  is not correct");
    }

    @Test
    void getTodayShouldReturnTwoExercisesWithoutWorkoutLink() {
        TodayServiceModel actual = this.workoutService.getAllWorkoutsForToday(CORRECT_LOCAL_DATE, CORRECT_USER_ID);
        Assertions.assertEquals(this.testCustomWorkout.getExercises().size(), actual.getWorkouts().get(1).getExercises().size(), "Workout  is not correct");
    }

    @Test
    void getTodayShouldReturnSortedExerciseOneWithoutWithLink() {
        TodayServiceModel actual = this.workoutService.getAllWorkoutsForToday(CORRECT_LOCAL_DATE, CORRECT_USER_ID);
        Assertions.assertEquals(2L, actual.getWorkouts().get(1).getExercises().get(0).getId(), "Workout  is not correct");
    }

    @Test
    void getTodayShouldReturnSortedExercisesWithWithoutLink2() {
        TodayServiceModel actual = this.workoutService.getAllWorkoutsForToday(CORRECT_LOCAL_DATE, CORRECT_USER_ID);
        Assertions.assertEquals(1L, actual.getWorkouts().get(1).getExercises().get(1).getId(), "Workout  is not correct");
    }

    @Test
    void getTodayShouldReturnSortedExerciseOneStatusUncompletedWithWithoutLink() {
        TodayServiceModel actual = this.workoutService.getAllWorkoutsForToday(CORRECT_LOCAL_DATE, CORRECT_USER_ID);
        Assertions.assertEquals(ExerciseStatusType.Uncompleted, actual.getWorkouts().get(1).getExercises().get(0).getStatus(), "Workout  is not correct");
    }

    @Test
    void getTodayShouldReturnSortedExerciseOneStatusCompletedWithWithoutLink() {
        TodayServiceModel actual = this.workoutService.getAllWorkoutsForToday(CORRECT_LOCAL_DATE, CORRECT_USER_ID);
        Assertions.assertEquals(ExerciseStatusType.Completed, actual.getWorkouts().get(1).getExercises().get(1).getStatus(), "Workout  is not correct");
    }

    @Test
    void getTodayShouldReturnCorrectExerciseTargetWithoutLink() {
        TodayServiceModel actual = this.workoutService.getAllWorkoutsForToday(CORRECT_LOCAL_DATE, CORRECT_USER_ID);
        Assertions.assertEquals(this.testExercise2.getTarget(), actual.getWorkouts().get(1).getExercises().get(1).getTarget(), "Workout  is not correct");
    }

    @Test
    void getTodayShouldReturnCorrectExerciseTypeWithoutLink() {
        TodayServiceModel actual = this.workoutService.getAllWorkoutsForToday(CORRECT_LOCAL_DATE, CORRECT_USER_ID);
        Assertions.assertEquals(this.testExercise2.getType(), actual.getWorkouts().get(1).getExercises().get(1).getType(), "Workout  is not correct");
    }

    @Test
    void getTodayShouldReturnCorrectExerciseDurationTypeWithoutLink() {
        TodayServiceModel actual = this.workoutService.getAllWorkoutsForToday(CORRECT_LOCAL_DATE, CORRECT_USER_ID);
        Assertions.assertEquals(this.testExercise2.getDurationType(), actual.getWorkouts().get(1).getExercises().get(1).getDurationType(), "Workout  is not correct");
    }

    @Test
    void getTodayShouldReturnCorrectExerciseIdWithoutLink() {
        TodayServiceModel actual = this.workoutService.getAllWorkoutsForToday(CORRECT_LOCAL_DATE, CORRECT_USER_ID);
        Assertions.assertEquals(this.testExercise2.getId(), actual.getWorkouts().get(1).getExercises().get(1).getId(), "Workout  is not correct");
    }

    @Test
    void getTodayWithWrongUserIdShouldThrowException() {
        Mockito.when(this.userService
                .checkIfUserExistsById(WRONG_USER_ID))
                .thenReturn(false);
        assertThrows(NotFoundException.class, () -> this.workoutService.getAllWorkoutsForToday(CORRECT_LOCAL_DATE, WRONG_USER_ID));
    }

    /**
     * WorkoutService - getWorkoutsStatsForDay(...) TESTS
     */
    @Test
    void getWorkoutsStatsForDayShouldReturnCorrectExerciseCount() {
        Mockito.when(
                this.workoutRepository.countAllExercisesForDayId(this.testDay.getId())
        ).thenReturn(Optional.of(this.testCustomWorkout.getExercises().size()));
        DayFullInfoServiceModel dayFullInfoServiceModel = new DayFullInfoServiceModel();
        dayFullInfoServiceModel.setId(this.testDay.getId());
        DayFullInfoServiceModel actual = this.workoutService.getWorkoutsStatsForDay(dayFullInfoServiceModel);
        Assertions.assertEquals(this.testCustomWorkout.getExercises().size(), actual.getTotalExercises(), "Workout  is not correct");
    }

    @Test
    void getWorkoutsStatsForDayShouldReturnCorrectWorkoutLinkCount() {
        Mockito.when(
                this.workoutRepository.countAllByDayIdAndLinkIsNotNull(this.testDay.getId())
        ).thenReturn(Optional.of(1));
        DayFullInfoServiceModel dayFullInfoServiceModel = new DayFullInfoServiceModel();
        dayFullInfoServiceModel.setId(this.testDay.getId());
        DayFullInfoServiceModel actual = this.workoutService.getWorkoutsStatsForDay(dayFullInfoServiceModel);
        Assertions.assertEquals(1, actual.getWorkoutsLinkCount(), "Workout  is not correct");
    }

    @Test
    void getWorkoutsStatsForDayShouldReturnCorrectWorkoutWithoutLinkCount() {
        Mockito.when(
                this.workoutRepository.countAllByDayIdAndLinkIsNull(this.testDay.getId())
        ).thenReturn(Optional.of(1));
        DayFullInfoServiceModel dayFullInfoServiceModel = new DayFullInfoServiceModel();
        dayFullInfoServiceModel.setId(this.testDay.getId());
        DayFullInfoServiceModel actual = this.workoutService.getWorkoutsStatsForDay(dayFullInfoServiceModel);
        Assertions.assertEquals(1, actual.getWorkoutsCount(), "Workout  is not correct");
    }

    @Test
    void getWorkoutsStatsForDayShouldReturnCorrectExerciseZeroCount() {
        Mockito.when(
                this.workoutRepository.countAllExercisesForDayId(this.testDay.getId())
        ).thenReturn(Optional.empty());
        DayFullInfoServiceModel dayFullInfoServiceModel = new DayFullInfoServiceModel();
        dayFullInfoServiceModel.setId(this.testDay.getId());
        DayFullInfoServiceModel actual = this.workoutService.getWorkoutsStatsForDay(dayFullInfoServiceModel);
        Assertions.assertEquals(0, actual.getTotalExercises(), "Workout  is not correct");
    }

    @Test
    void getWorkoutsStatsForDayShouldReturnCorrectWorkoutLinkZeroCount() {
        Mockito.when(
                this.workoutRepository.countAllByDayIdAndLinkIsNotNull(this.testDay.getId())
        ).thenReturn(Optional.empty());
        DayFullInfoServiceModel dayFullInfoServiceModel = new DayFullInfoServiceModel();
        dayFullInfoServiceModel.setId(this.testDay.getId());
        DayFullInfoServiceModel actual = this.workoutService.getWorkoutsStatsForDay(dayFullInfoServiceModel);
        Assertions.assertEquals(0, actual.getWorkoutsLinkCount(), "Workout  is not correct");
    }

    @Test
    void getWorkoutsStatsForDayShouldReturnCorrectWorkoutWithoutLinkZeroCount() {
        Mockito.when(
                this.workoutRepository.countAllByDayIdAndLinkIsNull(this.testDay.getId())
        ).thenReturn(Optional.empty());
        DayFullInfoServiceModel dayFullInfoServiceModel = new DayFullInfoServiceModel();
        dayFullInfoServiceModel.setId(this.testDay.getId());
        DayFullInfoServiceModel actual = this.workoutService.getWorkoutsStatsForDay(dayFullInfoServiceModel);
        Assertions.assertEquals(0, actual.getWorkoutsCount(), "Workout  is not correct");
    }
}
