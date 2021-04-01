package com.web.training.service;


import com.web.training.models.exceptionModels.HasTheSameTrainingException;
import com.web.training.models.exceptionModels.InvalidSortingTypeException;
import com.web.training.models.exceptionModels.NotFoundException;
import com.web.training.models.bindingModels.*;
import com.web.training.models.entities.*;
import com.web.training.models.enums.*;
import com.web.training.models.serviceModels.*;
import com.web.training.repositories.*;
import com.web.training.service.impl.TrainingServiceImpl;
import com.web.training.validators.SortingValidator;
import com.web.training.validators.impl.SortingValidatorImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@SpringBootTest
public class TrainingServiceTests {

    @Mock
    private User testUser;

    @Mock
    private TrainingService trainingService;
    @Mock
    private TrainingRepository trainingRepository;
    @Mock
    private DayRepository dayRepository;
    @Mock
    private WorkoutRepository workoutRepository;
    @Mock
    private ExerciseRepository exerciseRepository;
    @Mock
    private UserRepository userRepository;
    private final ModelMapper modelMapper = new ModelMapper();
    private final SortingValidator sortingValidator = new SortingValidatorImpl();


    private Training testTraining;
    private TrainingEditBindingModel trainingModel;
    private TrainingAddBindingModel trainingAddModel;
    private DayEditBindingModel editDayModel;
    private Day testDay;
    private Workout testLinkWorkout;
    private Workout testCustomWorkout;
    private Exercise testExercise;
    private SearchSuggestionBindingModel model;


    @BeforeEach
    public void init() {
        this.testUser = new User() {{
            setId(55L);
            setEnabled(true);
            setTrainings(new HashSet<>());
            setWeights(new HashSet<>());
            setFirstName("PeshoF");
            setLastName("PeshoL");
            setUsername("PeshoU");
            setEmail("Pesho@mail.bg");
            setAge(18);
            setPassword("asdfg");
            setHeight(184);
        }};
        this.testTraining = new Training() {{
            setId(234L);
            setFromDate(LocalDate.MIN);
            setTrainer(new HashSet<>());
            setUser(new User());
            setDifficulty(DifficultyType.Easy);
            setStatusType(StatusType.Completed);
            setDays(new HashSet<>());
            setToDate(LocalDate.MAX);
            setFromDate(LocalDate.now());
            setToDate(LocalDate.now());
            setTrainingType(TrainingType.Bike);
            setTitle("TRAINING_TITLE");
            setDescription("TRAINING_DESCRIPTION");
        }};

        trainingModel = new TrainingEditBindingModel();
        TrainingInfoEditBindingModel map = this.modelMapper.map(this.testTraining, TrainingInfoEditBindingModel.class);
        trainingModel.setTraining(map);
        trainingModel.setDays(new ArrayList<>());
        this.testDay = new Day() {{
            setDate(LocalDate.now());
            setWorkouts(new HashSet<>());
            setTraining(testTraining);
            setStatus(DayStatus.Future);
            setId(123L);
        }};
        this.testLinkWorkout = new Workout() {{
            setId(312L);
            setLink("https://demoLink.com");
            setType(WorkoutType.Link);
            setName("CORRECT_LINK_WORKOUT_NAME");
            setDay(testDay);
            setExercises(new HashSet<>());
        }};
        this.testCustomWorkout = new Workout() {{
            setId(12L);
            setName("CORRECT_CUSTOM_WORKOUT_NAME");
            setType(WorkoutType.Custom);
            setDay(testDay);
            setExercises(new HashSet<>());
        }};

        this.testExercise = new Exercise() {{
            setId(44L);
            setDuration(2);
            setType(ExerciseType.Plate_Hops);
            setDurationType(DurationType.min);
            setStatus(ExerciseStatusType.Uncompleted);
            setTarget(ExerciseTarget.Cadence);
            setWorkout(testCustomWorkout);
        }};
        this.testDay.getWorkouts().add(this.testLinkWorkout);
        this.testDay.getWorkouts().add(this.testCustomWorkout);
        this.testCustomWorkout.getExercises().add(this.testExercise);

        editDayModel = this.modelMapper.map(this.testDay, DayEditBindingModel.class);
        trainingModel.getDays().add(editDayModel);


        this.testUser.getTrainings().add(this.testTraining);
        this.trainingService = new TrainingServiceImpl(this.trainingRepository, this.dayRepository, this.workoutRepository, this.exerciseRepository
                , this.userRepository, this.sortingValidator, this.modelMapper);
    }

    void initMocks() {
        Mockito.when(this.trainingRepository
                .findById(this.testTraining.getId()))
                .thenReturn(Optional.of(this.testTraining));
        Mockito.when(this.trainingRepository
                .saveAndFlush(Mockito.any(Training.class)))
                .thenReturn(this.testTraining);
        Mockito.when(this.dayRepository
                .saveAndFlush(Mockito.any(Day.class)))
                .thenReturn(this.testDay);
        Mockito.when(this.workoutRepository
                .saveAndFlush(Mockito.any(Workout.class)))
                .thenReturn(this.testCustomWorkout);
    }

    void initMocks2() {
        Mockito.when(this.userRepository.findById(this.testUser.getId()))
                .thenReturn(Optional.of(this.testUser));
        Mockito.when(this.trainingRepository
                .getByIdAndUserId(this.testTraining.getId(), this.testUser.getId()))
                .thenReturn(Optional.of(this.testTraining));
        Mockito.when(this.trainingRepository
                .saveAndFlush(Mockito.any(Training.class)))
                .thenReturn(this.testTraining);
        Mockito.when(this.dayRepository
                .saveAndFlush(Mockito.any(Day.class)))
                .thenReturn(this.testDay);
        Mockito.when(this.workoutRepository
                .saveAndFlush(Mockito.any(Workout.class)))
                .thenReturn(this.testCustomWorkout);
    }

    void initMocks3() {
        this.model = new SearchSuggestionBindingModel();
        this.model.setNotIds(new ArrayList<>());
        Mockito.when(this.userRepository.findById(this.testUser.getId()))
                .thenReturn(Optional.of(this.testUser));

    }

    /**
     * TrainingService editReadyTrainingById(...) TESTS
     */

    @Test
    void editReadyTrainingByIdShouldReturnCorrectId() {
        initMocks();
        Mockito.when(this.exerciseRepository.findById(this.testExercise.getId())).thenReturn(Optional.of(this.testExercise));
        ReadyTrainingServiceModel actual = this.trainingService.editReadyTrainingById(this.testTraining.getId(), trainingModel);
        Assertions.assertEquals(this.testTraining.getId(), actual.getId(), "TrainingService  is not correct");
    }

    @Test
    void editReadyTrainingByIdShouldThrowException() {
        Mockito.when(this.trainingRepository
                .findById(this.testTraining.getId()))
                .thenReturn(Optional.of(this.testTraining));
        Assertions.assertThrows(NotFoundException.class,
                () -> this.trainingService.editReadyTrainingById(this.testTraining.getId() + 1, trainingModel)
                , "TrainingService  is not correct"
        );
    }

    /**
     * TrainingService editByTrainingById(...) TESTS
     */

    @Test
    void editByTrainingByIdShouldReturnCorrectId() {
        initMocks2();
        this.testTraining.setUser(this.testUser);
        Mockito.when(this.exerciseRepository.findById(this.testExercise.getId())).thenReturn(Optional.of(this.testExercise));
        TrainingServiceModel actual = this.trainingService.editByTrainingById(this.testUser.getId(), this.testTraining.getId(), trainingModel);
        Assertions.assertEquals(this.testTraining.getId(), actual.getId(), "TrainingService  is not correct");
    }

    @Test
    void editByTrainingByIdShouldThrowExceptionUser() {
        Mockito.when(this.userRepository.findById(this.testUser.getId()))
                .thenReturn(Optional.of(this.testUser));
        this.testTraining.setUser(this.testUser);
        Assertions.assertThrows(NotFoundException.class,
                () -> this.trainingService.editByTrainingById(this.testUser.getId() + 1, this.testTraining.getId(), trainingModel),
                "TrainingService  is not correct"
        );
    }

    @Test
    void editByTrainingByIdShouldThrowExceptionTraining() {
        Mockito.when(this.userRepository.findById(this.testUser.getId()))
                .thenReturn(Optional.of(this.testUser));
        Mockito.when(this.trainingRepository
                .getByIdAndUserId(this.testTraining.getId(), this.testUser.getId()))
                .thenReturn(Optional.of(this.testTraining));
        this.testTraining.setUser(this.testUser);
        Assertions.assertThrows(NotFoundException.class,
                () -> this.trainingService.editByTrainingById(this.testUser.getId(), this.testTraining.getId() + 2, trainingModel),
                "TrainingService  is not correct"
        );
    }

    /**
     * TrainingService getAllTrainingsForUserId(...) TESTS
     */

    @Test
    void getAllTrainingsForUserIdShouldReturnCorrectOneSize() {
        Mockito.when(this.userRepository.findById(this.testUser.getId()))
                .thenReturn(Optional.of(this.testUser));
        Mockito.when(this.trainingRepository
                .findAllByUserId(Mockito.eq(this.testUser.getId()), Mockito.any(Pageable.class)))
                .thenReturn(List.of(this.testTraining));
        TrainingsServiceModel actual = this.trainingService.getAllTrainingsForUserId(this.testUser.getId(), 0, 1, "fromDate", "asc");
        Assertions.assertEquals(1, actual.getTrainings().size(), "TrainingService is not correct");
    }

    @Test
    void getAllTrainingsForUserIdShouldReturnCorrectZeroSize() {
        Mockito.when(this.userRepository.findById(this.testUser.getId()))
                .thenReturn(Optional.of(this.testUser));
        Mockito.when(this.trainingRepository
                .findAllByUserId(Mockito.eq(this.testUser.getId()), Mockito.any(Pageable.class)))
                .thenReturn(List.of());
        TrainingsServiceModel actual = this.trainingService.getAllTrainingsForUserId(this.testUser.getId(), 0, 1, "fromDate", "asc");
        Assertions.assertEquals(0, actual.getTrainings().size(), "TrainingService is not correct");
    }

    @Test
    void getAllTrainingsForUserIdShouldReturnCorrectZeroNullSize() {
        Mockito.when(this.userRepository.findById(this.testUser.getId()))
                .thenReturn(Optional.of(this.testUser));
        Mockito.when(this.trainingRepository
                .findAllByUserId(Mockito.eq(this.testUser.getId()), Mockito.any(Pageable.class)))
                .thenReturn(null);
        TrainingsServiceModel actual = this.trainingService.getAllTrainingsForUserId(this.testUser.getId(), 0, 1, "fromDate", "asc");
        Assertions.assertEquals(0, actual.getTrainings().size(), "TrainingService is not correct");
    }

    @Test
    void getAllTrainingsForUserIdShouldReturnCorrectTrainingId() {
        Mockito.when(this.userRepository.findById(this.testUser.getId()))
                .thenReturn(Optional.of(this.testUser));
        Mockito.when(this.trainingRepository
                .findAllByUserId(Mockito.eq(this.testUser.getId()), Mockito.any(Pageable.class)))
                .thenReturn(List.of(this.testTraining));
        TrainingsServiceModel actual = this.trainingService.getAllTrainingsForUserId(this.testUser.getId(), 0, 1, "fromDate", "asc");
        Assertions.assertEquals(this.testTraining.getId(), actual.getTrainings().get(0).getId(), "TrainingService is not correct");
    }

    @Test
    void getAllTrainingsForUserIdShouldReturnCorrectTrainingIdDesc() {
        Mockito.when(this.userRepository.findById(this.testUser.getId()))
                .thenReturn(Optional.of(this.testUser));
        Mockito.when(this.trainingRepository
                .findAllByUserId(Mockito.eq(this.testUser.getId()), Mockito.any(Pageable.class)))
                .thenReturn(List.of(this.testTraining));
        TrainingsServiceModel actual = this.trainingService.getAllTrainingsForUserId(this.testUser.getId(), 0, 1, "fromDate", "desc");
        Assertions.assertEquals(this.testTraining.getId(), actual.getTrainings().get(0).getId(), "TrainingService is not correct");
    }

    @Test
    void getAllTrainingsForUserIdShouldReturnCorrectTrainingTitle() {
        Mockito.when(this.userRepository.findById(this.testUser.getId()))
                .thenReturn(Optional.of(this.testUser));
        Mockito.when(this.trainingRepository
                .findAllByUserId(Mockito.eq(this.testUser.getId()), Mockito.any(Pageable.class)))
                .thenReturn(List.of(this.testTraining));
        TrainingsServiceModel actual = this.trainingService.getAllTrainingsForUserId(this.testUser.getId(), 0, 1, "fromDate", "asc");
        Assertions.assertEquals(this.testTraining.getTitle(), actual.getTrainings().get(0).getTitle(), "TrainingService is not correct");
    }

    @Test
    void getAllTrainingsForUserIdShouldReturnCorrectTrainingDescription() {
        Mockito.when(this.userRepository.findById(this.testUser.getId()))
                .thenReturn(Optional.of(this.testUser));
        Mockito.when(this.trainingRepository
                .findAllByUserId(Mockito.eq(this.testUser.getId()), Mockito.any(Pageable.class)))
                .thenReturn(List.of(this.testTraining));
        TrainingsServiceModel actual = this.trainingService.getAllTrainingsForUserId(this.testUser.getId(), 0, 1, "fromDate", "asc");
        Assertions.assertEquals(this.testTraining.getDescription(), actual.getTrainings().get(0).getDescription(), "TrainingService is not correct");
    }

    @Test
    void getAllTrainingsForUserIdShouldReturnCorrectTrainingTrainingType() {
        Mockito.when(this.userRepository.findById(this.testUser.getId()))
                .thenReturn(Optional.of(this.testUser));
        Mockito.when(this.trainingRepository
                .findAllByUserId(Mockito.eq(this.testUser.getId()), Mockito.any(Pageable.class)))
                .thenReturn(List.of(this.testTraining));
        TrainingsServiceModel actual = this.trainingService.getAllTrainingsForUserId(this.testUser.getId(), 0, 1, "fromDate", "asc");
        Assertions.assertEquals(this.testTraining.getTrainingType(), actual.getTrainings().get(0).getTrainingType(), "TrainingService is not correct");
    }

    @Test
    void getAllTrainingsForUserIdShouldReturnCorrectTotalPages() {
        Mockito.when(this.userRepository.findById(this.testUser.getId()))
                .thenReturn(Optional.of(this.testUser));
        Mockito.when(this.trainingRepository
                .findAllByUserId(Mockito.eq(this.testUser.getId()), Mockito.any(Pageable.class)))
                .thenReturn(List.of(this.testTraining));
        Mockito.when(this.trainingRepository
                .getAllByUserId(Mockito.eq(this.testUser.getId())))
                .thenReturn(List.of(this.testTraining));
        TrainingsServiceModel actual = this.trainingService.getAllTrainingsForUserId(this.testUser.getId(), 0, 1, "fromDate", "asc");
        Assertions.assertEquals(0, actual.getTotalPages(), "TrainingService is not correct");
    }

    @Test
    void getAllTrainingsForUserIdShouldReturnCorrectZeroTotalPages() {
        Mockito.when(this.userRepository.findById(this.testUser.getId()))
                .thenReturn(Optional.of(this.testUser));
        Mockito.when(this.trainingRepository
                .findAllByUserId(Mockito.eq(this.testUser.getId()), Mockito.any(Pageable.class)))
                .thenReturn(List.of());
        Mockito.when(this.trainingRepository
                .getAllByUserId(Mockito.eq(this.testUser.getId())))
                .thenReturn(new ArrayList<>());
        TrainingsServiceModel actual = this.trainingService.getAllTrainingsForUserId(this.testUser.getId(), 0, 1, "fromDate", "asc");

        Assertions.assertEquals(0, actual.getTotalPages(), "TrainingService is not correct");
    }

    @Test
    void getAllTrainingsForUserIdShouldReturnCorrectOneTotalPages() {
        Mockito.when(this.userRepository.findById(this.testUser.getId()))
                .thenReturn(Optional.of(this.testUser));
        Mockito.when(this.trainingRepository
                .findAllByUserId(Mockito.eq(this.testUser.getId()), Mockito.any(Pageable.class)))
                .thenReturn(List.of(this.testTraining, this.testTraining));
        Mockito.when(this.trainingRepository
                .getAllByUserId(Mockito.eq(this.testUser.getId())))
                .thenReturn(List.of(this.testTraining, this.testTraining));
        TrainingsServiceModel actual = this.trainingService.getAllTrainingsForUserId(this.testUser.getId(), 0, 1, "fromDate", "asc");
        Assertions.assertEquals(1, actual.getTotalPages(), "TrainingService is not correct");
    }

    @Test
    void getAllTrainingsForUserIdShouldThrowInvalidSortingException() {
        Mockito.when(this.userRepository.findById(this.testUser.getId()))
                .thenReturn(Optional.of(this.testUser));
        Mockito.when(this.trainingRepository
                .findAllByUserId(Mockito.eq(this.testUser.getId()), Mockito.any(Pageable.class)))
                .thenReturn(List.of(this.testTraining, this.testTraining));
        Mockito.when(this.trainingRepository
                .getAllByUserId(Mockito.eq(this.testUser.getId())))
                .thenReturn(List.of(this.testTraining, this.testTraining));
        Assertions.assertThrows(InvalidSortingTypeException.class,
                () -> this.trainingService.getAllTrainingsForUserId(this.testUser.getId(), 0, 1, "age", "asc"),
                "TrainingService is not correct"
        );
    }

    @Test
    void getAllTrainingsForUserIdShouldThrowInvalidUserException() {
        Mockito.when(this.userRepository.findById(this.testUser.getId()))
                .thenReturn(Optional.of(this.testUser));
        Assertions.assertThrows(NotFoundException.class,
                () -> this.trainingService.getAllTrainingsForUserId(this.testUser.getId() + 1, 0, 1, "age", "asc"),
                "TrainingService is not correct"
        );
    }

    /**
     * TrainingService addNewTraining(...) TESTS
     */

    @Test
    void addNewTrainingShouldReturnCorrectCorrectId() {
        this.trainingAddModel = this.modelMapper.map(this.testTraining, TrainingAddBindingModel.class);
        this.trainingAddModel.setDays(this.trainingModel.getDays().stream().map(el -> this.modelMapper.map(el, DayAddBindingModel.class))
                .collect(Collectors.toSet()));
        Mockito.when(this.trainingRepository
                .saveAndFlush(Mockito.any(Training.class)))
                .thenReturn(this.testTraining);
        Mockito.when(this.dayRepository
                .saveAndFlush(Mockito.any(Day.class)))
                .thenReturn(this.testDay);
        Mockito.when(this.workoutRepository
                .saveAndFlush(Mockito.any(Workout.class)))
                .thenReturn(this.testCustomWorkout);


        Mockito.when(this.userRepository.findById(this.testUser.getId()))
                .thenReturn(Optional.of(this.testUser));
        Mockito.when(this.trainingRepository
                .countAllByUserIdAndTitleStartingWith(this.testUser.getId(), this.testTraining.getTitle()))
                .thenReturn(Optional.empty());
        TrainingServiceModel actual = this.trainingService.addNewTraining(this.testUser.getId(), this.trainingAddModel);
        Assertions.assertEquals(this.testTraining.getId(), actual.getId(), "TrainingService is not correct");
    }

    @Test
    void addNewTrainingShouldThrowAlreadyHasTrainingException() {
        this.trainingAddModel = new TrainingAddBindingModel();
        this.trainingAddModel.setTitle("SADF");
        this.testTraining.setTitle("SADF");
        Mockito.when(this.userRepository.findById(this.testUser.getId()))
                .thenReturn(Optional.of(this.testUser));
        Mockito.when(this.trainingRepository
                .countAllByUserIdAndTitleStartingWith(this.testUser.getId(), this.testTraining.getTitle()))
                .thenReturn(Optional.of(1));
        Assertions.assertThrows(HasTheSameTrainingException.class,
                () -> this.trainingService.addNewTraining(this.testUser.getId(), this.trainingAddModel),
                "TrainingService is not correct"
        );
    }

    @Test
    void addNewTrainingShouldThrowInvalidUserException() {
        Mockito.when(this.userRepository.findById(this.testUser.getId()))
                .thenReturn(Optional.of(this.testUser));
        Assertions.assertThrows(NotFoundException.class,
                () -> this.trainingService.addNewTraining(this.testUser.getId() + 1, this.trainingAddModel),
                "TrainingService is not correct"
        );
    }

    /**
     * TrainingService getTrainingWithDayLinks(...) TESTS
     */

    @Test
    void getTrainingWithDayLinksShouldReturnCorrectDaySize() {
        this.testTraining.getDays().add(this.testDay);
        Mockito.when(this.userRepository.findById(this.testUser.getId()))
                .thenReturn(Optional.of(this.testUser));
        Mockito.when(this.trainingRepository
                .getByIdAndUserId(this.testTraining.getId(), this.testUser.getId()))
                .thenReturn(Optional.of(this.testTraining));
        TrainingWithLinksServiceModel actual = this.trainingService.getTrainingWithDayLinks(this.testUser.getId(), this.testTraining.getId());
        Assertions.assertEquals(1, actual.getDays().size(), "TrainingService is not correct");
    }

    @Test
    void getTrainingWithDayLinksShouldReturnCorrectWorkoutSize() {
        this.testTraining.getDays().add(this.testDay);
        Mockito.when(this.userRepository.findById(this.testUser.getId()))
                .thenReturn(Optional.of(this.testUser));
        Mockito.when(this.trainingRepository
                .getByIdAndUserId(this.testTraining.getId(), this.testUser.getId()))
                .thenReturn(Optional.of(this.testTraining));
        TrainingWithLinksServiceModel actual = this.trainingService.getTrainingWithDayLinks(this.testUser.getId(), this.testTraining.getId());
        Assertions.assertEquals(2, actual.getDays().get(0).getWorkoutsCount(), "TrainingService is not correct");
    }

    @Test
    void getTrainingWithDayLinksShouldThrowException() {
        this.testTraining.getDays().add(this.testDay);
        Mockito.when(this.userRepository.findById(this.testUser.getId()))
                .thenReturn(Optional.of(this.testUser));
        Mockito.when(this.trainingRepository
                .getByIdAndUserId(this.testTraining.getId(), this.testUser.getId()))
                .thenReturn(Optional.of(this.testTraining));
        Assertions.assertThrows(NotFoundException.class,
                () -> this.trainingService.getTrainingWithDayLinks(this.testUser.getId(), this.testTraining.getId() + 1),
                "TrainingService is not correct"
        );
    }

    @Test
    void getTrainingWithDayLinksShouldThrowExceptionUser() {
        this.testTraining.getDays().add(this.testDay);
        Mockito.when(this.userRepository.findById(this.testUser.getId()))
                .thenReturn(Optional.of(this.testUser));
        Assertions.assertThrows(NotFoundException.class,
                () -> this.trainingService.getTrainingWithDayLinks(this.testUser.getId() + 1, this.testTraining.getId()),
                "TrainingService is not correct"
        );
    }


    /**
     * TrainingService getFullTraining(...) TESTS
     */

    @Test
    void getFullTrainingShouldThrowException() {
        this.testTraining.getDays().add(this.testDay);
        Mockito.when(this.userRepository.findById(this.testUser.getId()))
                .thenReturn(Optional.of(this.testUser));
        Mockito.when(this.trainingRepository
                .getByIdAndUserId(this.testTraining.getId(), this.testUser.getId()))
                .thenReturn(Optional.of(this.testTraining));
        Assertions.assertThrows(NotFoundException.class,
                () -> this.trainingService.getFullTraining(this.testUser.getId(), this.testTraining.getId() + 1),
                "TrainingService is not correct"
        );
    }

    @Test
    void getFullTrainingShouldThrowExceptionUser() {
        this.testTraining.getDays().add(this.testDay);
        Mockito.when(this.userRepository.findById(this.testUser.getId()))
                .thenReturn(Optional.of(this.testUser));
        Assertions.assertThrows(NotFoundException.class,
                () -> this.trainingService.getFullTraining(this.testUser.getId() + 1, this.testTraining.getId()),
                "TrainingService is not correct"
        );
    }

    @Test
    void getFullTrainingShouldReturnCorrectDaySize() {
        this.testTraining.getDays().add(this.testDay);
        Mockito.when(this.userRepository.findById(this.testUser.getId()))
                .thenReturn(Optional.of(this.testUser));
        Mockito.when(this.trainingRepository
                .getByIdAndUserId(this.testTraining.getId(), this.testUser.getId()))
                .thenReturn(Optional.of(this.testTraining));
        TrainingFullInfoServiceModel actual = this.trainingService.getFullTraining(this.testUser.getId(), this.testTraining.getId());
        Assertions.assertEquals(1, actual.getDays().size(), "TrainingService is not correct");
    }

    @Test
    void getFullTrainingShouldReturnCorrectWorkoutSize() {
        this.testTraining.getDays().add(this.testDay);
        Mockito.when(this.userRepository.findById(this.testUser.getId()))
                .thenReturn(Optional.of(this.testUser));
        Mockito.when(this.trainingRepository
                .getByIdAndUserId(this.testTraining.getId(), this.testUser.getId()))
                .thenReturn(Optional.of(this.testTraining));
        TrainingFullInfoServiceModel actual = this.trainingService.getFullTraining(this.testUser.getId(), this.testTraining.getId());
        Assertions.assertEquals(2, actual.getDays().get(0).getWorkouts().size(), "TrainingService is not correct");
    }

    /**
     * TrainingService addNewReadyTraining(...) TESTS
     */

    @Test
    void addNewReadyTrainingShouldReturnCorrectCorrectId() {
        this.trainingAddModel = this.modelMapper.map(this.testTraining, TrainingAddBindingModel.class);
        this.trainingAddModel.setDays(this.trainingModel.getDays().stream().map(el -> this.modelMapper.map(el, DayAddBindingModel.class))
                .collect(Collectors.toSet()));
        Mockito.when(this.trainingRepository
                .saveAndFlush(Mockito.any(Training.class)))
                .thenReturn(this.testTraining);
        Mockito.when(this.dayRepository
                .saveAndFlush(Mockito.any(Day.class)))
                .thenReturn(this.testDay);
        Mockito.when(this.workoutRepository
                .saveAndFlush(Mockito.any(Workout.class)))
                .thenReturn(this.testCustomWorkout);


        Mockito.when(this.userRepository.findById(this.testUser.getId()))
                .thenReturn(Optional.of(this.testUser));
        Mockito.when(this.trainingRepository
                .countAllByUserIdAndTitleStartingWith(this.testUser.getId(), this.testTraining.getTitle()))
                .thenReturn(Optional.empty());
        ReadyTrainingServiceModel actual = this.trainingService.addNewReadyTraining(this.trainingAddModel);
        Assertions.assertEquals(this.testTraining.getId(), actual.getId(), "TrainingService is not correct");
    }

    /**
     * TrainingService getSuggestionsByTitle(...) TESTS
     */

    @Test
    void getSuggestionsByUsernameShouldReturnEmptyList() {
        initMocks3();
        model.setInput("WRONG_TITLE");
        List<TrainingServiceModel> actual = this.trainingService.getSuggestionsByTitle(this.testUser.getId(), model);
        Assertions.assertEquals(0, actual.size(), "TrainingService is not correct");
    }

    @Test
    void getSuggestionsByUsernameShouldReturnListWithSizeTwo() {
        initMocks3();
        model.setInput(this.testUser.getUsername());
        Mockito.when(this.trainingRepository.getTop5ByTitleStartsWithAndUser_Id(model.getInput(), this.testUser.getId()))
                .thenReturn(List.of(this.testTraining));
        List<TrainingServiceModel> actual = this.trainingService.getSuggestionsByTitle(this.testUser.getId(), model);
        Assertions.assertEquals(1, actual.size(), "TrainingService is not correct");
    }

    @Test
    void getSuggestionsByUsernameShouldReturnListWithSizeTwoWithNotIdsZeroCount() {
        initMocks3();
        model.getNotIds().add(3L);
        model.setInput("dsfa");
        Mockito.when(this.trainingRepository.getTop5ByTitleStartsWithAndIdNotInAndUser_Id(model.getInput(), model.getNotIds(), this.testUser.getId()))
                .thenReturn(List.of(this.testTraining));
        List<TrainingServiceModel> actual = this.trainingService.getSuggestionsByTitle(this.testUser.getId(), model);
        Assertions.assertEquals(1, actual.size(), "TrainingService is not correct");
    }

    @Test
    void getSuggestionsByWrongUsernameShouldReturnListWithSizeZeroCount() {
        initMocks3();
        model.setNotIds(new ArrayList<>());
        List<TrainingServiceModel> actual = this.trainingService.getSuggestionsByTitle(this.testUser.getId(), model);
        Assertions.assertEquals(0, actual.size(), "TrainingService is not correct");
    }

    @Test
    void getSuggestionsByUsernameShouldReturnListElementOneCorrectId() {
        initMocks3();
        model.setInput(this.testTraining.getTitle());
        model.getNotIds().add(5L);
        Mockito.when(this.trainingRepository.getTop5ByTitleStartsWithAndIdNotInAndUser_Id(model.getInput(), model.getNotIds(), this.testUser.getId()))
                .thenReturn(List.of(this.testTraining));
        List<TrainingServiceModel> actual = this.trainingService.getSuggestionsByTitle(this.testUser.getId(), model);
        Assertions.assertEquals(this.testTraining.getId(), actual.get(0).getId(), "TrainingService is not correct");
    }

    @Test
    void getSuggestionsByTitleShouldThrowException() {
        Mockito.when(this.userRepository.findById(this.testUser.getId()))
                .thenReturn(Optional.of(this.testUser));
        Assertions.assertThrows(NotFoundException.class,
                () -> this.trainingService.getSuggestionsByTitle(this.testUser.getId() + 1, this.model),
                "TrainingService is not correct"
        );
    }

    /**
     * TrainingService addReadyTrainingToUser(...) TESTS
     */

    @Test
    void addReadyTrainingToUserShouldReturnCorrectId() {
        this.testTraining.getDays().add(this.testDay);
        Mockito.when(this.trainingRepository
                .saveAndFlush(Mockito.any(Training.class)))
                .thenReturn(this.testTraining);
        Mockito.when(this.dayRepository
                .saveAndFlush(Mockito.any(Day.class)))
                .thenReturn(this.testDay);
        Mockito.when(this.workoutRepository
                .saveAndFlush(Mockito.any(Workout.class)))
                .thenReturn(this.testCustomWorkout);

        Mockito.when(this.trainingRepository.findById(this.testTraining.getId()))
                .thenReturn(Optional.of(this.testTraining));
        Mockito.when(this.userRepository.findById(this.testUser.getId()))
                .thenReturn(Optional.of(this.testUser));
        Mockito.when(this.trainingRepository
                .countAllByUserIdAndTitleStartingWith(this.testUser.getId(), this.testTraining.getTitle()))
                .thenReturn(Optional.empty());
        TrainingServiceModel actual = this.trainingService
                .addReadyTrainingToUser(this.testTraining.getId(), this.testUser.getId(), LocalDate.now());
        Assertions.assertEquals(this.testTraining.getTitle(), actual.getTitle(), "TrainingService is not correct");
    }

    @Test
    void addReadyTrainingToUserShouldThrowException() {
        Mockito.when(this.userRepository.findById(this.testUser.getId()))
                .thenReturn(Optional.of(this.testUser));
        Assertions.assertThrows(NotFoundException.class,
                () -> this.trainingService
                        .addReadyTrainingToUser(this.testTraining.getId() + 1, this.testUser.getId(), LocalDate.now()),
                "TrainingService is not correct"
        );
    }

    @Test
    void addReadyTrainingToUserShouldThrowException2() {
        Mockito.when(this.userRepository.findById(this.testUser.getId()))
                .thenReturn(Optional.of(this.testUser));
        Assertions.assertThrows(NotFoundException.class,
                () -> this.trainingService
                        .addReadyTrainingToUser(this.testTraining.getId(), this.testUser.getId() + 1, LocalDate.now()),
                "TrainingService is not correct"
        );
    }

    /**
     * TrainingService getTrainingsStatistics(...) TESTS
     */
    @Test
    void getTrainingsStatisticsShouldReturnCorrectDataSize() {
        Mockito.when(this.userRepository.findById(this.testUser.getId()))
                .thenReturn(Optional.of(this.testUser));
        Mockito.when(
                this.trainingRepository.countAllByUserIdAndStatusType(this.testUser.getId(), StatusType.Completed)
        ).thenReturn(Optional.of(1));
        Mockito.when(
                this.trainingRepository.countAllByUserIdAndStatusType(this.testUser.getId(), StatusType.InProgress)
        ).thenReturn(Optional.of(1));
        Mockito.when(
                this.trainingRepository.countAllByUserIdAndStatusType(this.testUser.getId(), StatusType.Future)
        ).thenReturn(Optional.of(1));

        TrainingStatisticServiceModel actual = this.trainingService.getTrainingsStatistics(this.testUser.getId());
        Assertions.assertEquals(3, actual.getData().size(), "TrainingService is not correct");
    }

    @Test
    void getTrainingsStatisticsShouldReturnCorrectLabelSize() {
        Mockito.when(this.userRepository.findById(this.testUser.getId()))
                .thenReturn(Optional.of(this.testUser));
        Mockito.when(
                this.trainingRepository.countAllByUserIdAndStatusType(this.testUser.getId(), StatusType.Completed)
        ).thenReturn(Optional.of(1));
        Mockito.when(
                this.trainingRepository.countAllByUserIdAndStatusType(this.testUser.getId(), StatusType.InProgress)
        ).thenReturn(Optional.of(1));
        Mockito.when(
                this.trainingRepository.countAllByUserIdAndStatusType(this.testUser.getId(), StatusType.Future)
        ).thenReturn(Optional.of(1));

        TrainingStatisticServiceModel actual = this.trainingService.getTrainingsStatistics(this.testUser.getId());
        Assertions.assertEquals(3, actual.getLabels().size(), "TrainingService is not correct");
    }

    @Test
    void getTrainingsStatisticsShouldReturnCorrectLabelFuture() {
        Mockito.when(this.userRepository.findById(this.testUser.getId()))
                .thenReturn(Optional.of(this.testUser));
        Mockito.when(
                this.trainingRepository.countAllByUserIdAndStatusType(this.testUser.getId(), StatusType.Completed)
        ).thenReturn(Optional.of(1));
        Mockito.when(
                this.trainingRepository.countAllByUserIdAndStatusType(this.testUser.getId(), StatusType.InProgress)
        ).thenReturn(Optional.of(1));
        Mockito.when(
                this.trainingRepository.countAllByUserIdAndStatusType(this.testUser.getId(), StatusType.Future)
        ).thenReturn(Optional.of(1));

        TrainingStatisticServiceModel actual = this.trainingService.getTrainingsStatistics(this.testUser.getId());
        Assertions.assertTrue(actual.getLabels().contains(StatusType.Future.name()), "TrainingService is not correct");
    }

    @Test
    void getTrainingsStatisticsShouldReturnCorrectLabelInProgress() {
        Mockito.when(this.userRepository.findById(this.testUser.getId()))
                .thenReturn(Optional.of(this.testUser));
        Mockito.when(
                this.trainingRepository.countAllByUserIdAndStatusType(this.testUser.getId(), StatusType.Completed)
        ).thenReturn(Optional.of(1));
        Mockito.when(
                this.trainingRepository.countAllByUserIdAndStatusType(this.testUser.getId(), StatusType.InProgress)
        ).thenReturn(Optional.of(1));
        Mockito.when(
                this.trainingRepository.countAllByUserIdAndStatusType(this.testUser.getId(), StatusType.Future)
        ).thenReturn(Optional.of(1));

        TrainingStatisticServiceModel actual = this.trainingService.getTrainingsStatistics(this.testUser.getId());
        Assertions.assertTrue(actual.getLabels().contains(StatusType.InProgress.name()), "TrainingService is not correct");
    }

    @Test
    void getTrainingsStatisticsShouldReturnCorrectLabelCompleted() {
        Mockito.when(this.userRepository.findById(this.testUser.getId()))
                .thenReturn(Optional.of(this.testUser));
        Mockito.when(
                this.trainingRepository.countAllByUserIdAndStatusType(this.testUser.getId(), StatusType.Completed)
        ).thenReturn(Optional.of(1));
        Mockito.when(
                this.trainingRepository.countAllByUserIdAndStatusType(this.testUser.getId(), StatusType.InProgress)
        ).thenReturn(Optional.of(1));
        Mockito.when(
                this.trainingRepository.countAllByUserIdAndStatusType(this.testUser.getId(), StatusType.Future)
        ).thenReturn(Optional.of(1));

        TrainingStatisticServiceModel actual = this.trainingService.getTrainingsStatistics(this.testUser.getId());
        Assertions.assertTrue(actual.getLabels().contains(StatusType.Completed.name()), "TrainingService is not correct");
    }

    @Test
    void getTrainingsStatisticsShouldReturnCorrectZeroDataFuture() {
        Mockito.when(this.userRepository.findById(this.testUser.getId()))
                .thenReturn(Optional.of(this.testUser));
        Mockito.when(
                this.trainingRepository.countAllByUserIdAndStatusType(this.testUser.getId(), StatusType.Completed)
        ).thenReturn(Optional.of(1));
        Mockito.when(
                this.trainingRepository.countAllByUserIdAndStatusType(this.testUser.getId(), StatusType.InProgress)
        ).thenReturn(Optional.of(1));
        Mockito.when(
                this.trainingRepository.countAllByUserIdAndStatusType(this.testUser.getId(), StatusType.Future)
        ).thenReturn(Optional.empty());

        TrainingStatisticServiceModel actual = this.trainingService.getTrainingsStatistics(this.testUser.getId());
        Assertions.assertEquals(1L, actual.getData().stream().filter(el -> el == 0).count(), "TrainingService is not correct");
    }

    @Test
    void getTrainingsStatisticsShouldReturnCorrectZeroDataInProgress() {
        Mockito.when(this.userRepository.findById(this.testUser.getId()))
                .thenReturn(Optional.of(this.testUser));
        Mockito.when(
                this.trainingRepository.countAllByUserIdAndStatusType(this.testUser.getId(), StatusType.Completed)
        ).thenReturn(Optional.of(1));
        Mockito.when(
                this.trainingRepository.countAllByUserIdAndStatusType(this.testUser.getId(), StatusType.InProgress)
        ).thenReturn(Optional.empty());
        Mockito.when(
                this.trainingRepository.countAllByUserIdAndStatusType(this.testUser.getId(), StatusType.Future)
        ).thenReturn(Optional.of(1));

        TrainingStatisticServiceModel actual = this.trainingService.getTrainingsStatistics(this.testUser.getId());
        Assertions.assertEquals(1L, actual.getData().stream().filter(el -> el == 0).count(), "TrainingService is not correct");
    }

    @Test
    void getTrainingsStatisticsShouldReturnCorrectZeroDataCompleted() {
        Mockito.when(this.userRepository.findById(this.testUser.getId()))
                .thenReturn(Optional.of(this.testUser));
        Mockito.when(
                this.trainingRepository.countAllByUserIdAndStatusType(this.testUser.getId(), StatusType.Completed)
        ).thenReturn(Optional.empty());
        Mockito.when(
                this.trainingRepository.countAllByUserIdAndStatusType(this.testUser.getId(), StatusType.InProgress)
        ).thenReturn(Optional.of(1));
        Mockito.when(
                this.trainingRepository.countAllByUserIdAndStatusType(this.testUser.getId(), StatusType.Future)
        ).thenReturn(Optional.of(1));

        TrainingStatisticServiceModel actual = this.trainingService.getTrainingsStatistics(this.testUser.getId());
        Assertions.assertEquals(1L, actual.getData().stream().filter(el -> el == 0).count(), "TrainingService is not correct");
    }

    @Test
    void getTrainingsStatisticsShouldThrowException() {
        Mockito.when(this.userRepository.findById(this.testUser.getId()))
                .thenReturn(Optional.of(this.testUser));
        Assertions.assertThrows(NotFoundException.class,
                () -> this.trainingService
                        .getTrainingsStatistics(this.testTraining.getId() + 1),
                "TrainingService is not correct"
        );
    }

}
