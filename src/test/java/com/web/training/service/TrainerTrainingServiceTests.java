package com.web.training.service;


import com.web.training.models.exceptionModels.NotFoundException;
import com.web.training.models.bindingModels.TrainingEditBindingModel;
import com.web.training.models.entities.*;
import com.web.training.models.enums.DifficultyType;
import com.web.training.models.enums.RelationStatus;
import com.web.training.models.enums.StatusType;
import com.web.training.models.enums.TrainerType;
import com.web.training.models.serviceModels.ReadyTrainingServiceModel;
import com.web.training.models.serviceModels.TrainingBasicInfoServiceModel;
import com.web.training.models.serviceModels.TrainingFullInfoServiceModel;
import com.web.training.repositories.TrainerRepository;
import com.web.training.repositories.TrainerTrainingRepository;
import com.web.training.repositories.TrainingRepository;
import com.web.training.service.impl.TrainerTrainingServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@SpringBootTest
public class TrainerTrainingServiceTests {

    @Mock
    private TrainerTrainingRepository trainerTrainingRepository;
    @Mock
    private TrainerRepository trainerRepository;
    @Mock
    private TrainingRepository trainingRepository;
    @Mock
    private TrainingService trainingService;


    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private ModelMapper modelMapper = new ModelMapper();

    private TrainerTrainingService trainerTrainingService;


    private static final Long WRONG_ID = 13L;

    private Trainer testTrainer;
    private Trainer testTrainer2;
    private TrainerTraining testTrainerTraining;
    private TrainerTraining testTrainerTraining2;
    private User testUser;
    private Training testTraining2;
    private Training testTraining;

    @BeforeEach
    public void init() {
        this.testTrainer2 = new Trainer();
        this.testTraining2 = new Training() {{
            setId(2L);
            setTitle("SAD");
            setDifficulty(DifficultyType.Easy);
            setDays(new HashSet<>());
            setTrainer(new HashSet<>());
            setToDate(LocalDate.MAX);
            setFromDate(LocalDate.MAX);
            setStatusType(StatusType.Future);
        }};
        this.testTraining = new Training() {{
            setId(1L);
            setTitle("SAD");
            setDifficulty(DifficultyType.Easy);
            setDays(new HashSet<>());
            setTrainer(new HashSet<>());
            setToDate(LocalDate.MAX);
            setFromDate(LocalDate.MAX);
            setStatusType(StatusType.Future);
        }};
        this.testTrainerTraining = new TrainerTraining() {{
            setTrainer(testTrainer);
            setTraining(testTraining);
            setId(1L);
        }};
        this.testTrainerTraining2 = new TrainerTraining() {{
            setTrainer(testTrainer2);
            setTraining(testTraining2);
            setId(2L);
        }};

        Picture a = new Picture();
        a.setLocation("ads");


        this.testUser = new User() {{
            setId(1231L);
            setEnabled(true);
            setPicture(a);
            setAuthorities(new ArrayList<>());
            setTrainer(testTrainer);
            setTrainers(new HashSet<>());
            setTrainings(new HashSet<>());
            setWeights(new HashSet<>());
            setFirstName("PeshoF");
            setLastName("PeshoL");
            setUsername("PeshoU");
            setEmail("Pesho@mail.bg");
            setAge(18);
            setPassword(passwordEncoder.encode("asdfg"));
            setHeight(184);
        }};
        this.testUser.getAuthorities().add(new Authority());
        this.testTrainer = new Trainer() {{
            setId(345L);
            setFromDate(LocalDate.now());
            setType(TrainerType.Group);
            setPhoneNumber("+359887473527");
            setTrainings(new HashSet<>());
            setUser(testUser);
            setStatus(RelationStatus.Accepted);
        }};


        this.trainerTrainingService = new TrainerTrainingServiceImpl(
                this.trainerTrainingRepository, this.trainerRepository, this.trainingRepository,
                this.trainingService, this.modelMapper
        );
    }

    /**
     * TrainerTrainingService - addNewTrainingForTrainer(...) TESTS
     */

    @Test
    void addNewTrainingForTrainerShouldAddItCorrectly() {
        Mockito.when(
                this.trainingRepository.findById(this.testTraining.getId()))
                .thenReturn(Optional.of(this.testTraining));
        Mockito.when(
                this.trainerRepository.getByIdAndStatus(this.testTrainer.getId(), RelationStatus.Accepted))
                .thenReturn(Optional.of(this.testTrainer));
        Assertions.assertDoesNotThrow(
                () -> this.trainerTrainingService.addNewTrainingForTrainer(this.testTraining.getId(), this.testTrainer.getId()),
                "TrainerTraining is not correct"
        );
    }

    @Test
    void addNewTrainingForTrainerShouldThrowNoTrainer() {
        Mockito.when(
                this.trainingRepository.findById(this.testTraining.getId()))
                .thenReturn(Optional.of(this.testTraining));
        Mockito.when(
                this.trainerRepository.getByIdAndStatus(this.testTrainer.getId(), RelationStatus.Accepted))
                .thenReturn(Optional.of(this.testTrainer));
        Assertions.assertThrows(NotFoundException.class,
                () -> this.trainerTrainingService.addNewTrainingForTrainer(this.testTraining.getId(), WRONG_ID),
                "TrainerTraining is not correct"
        );
    }

    @Test
    void addNewTrainingForTrainerShouldThrowNoTraining() {
        Mockito.when(
                this.trainingRepository.findById(this.testTraining.getId()))
                .thenReturn(Optional.of(this.testTraining));
        Mockito.when(
                this.trainerRepository.getByIdAndStatus(this.testTrainer.getId(), RelationStatus.Accepted))
                .thenReturn(Optional.of(this.testTrainer));
        Assertions.assertThrows(NotFoundException.class,
                () -> this.trainerTrainingService.addNewTrainingForTrainer(WRONG_ID, this.testTrainer.getId()),
                "TrainerTraining is not correct"
        );
    }

    /**
     * TrainerTrainingService - getReadyTrainings(...) TESTS
     */

    @Test
    void getReadyTrainingsShouldReturnCorrectSize() {
        Mockito.when(
                this.trainerTrainingRepository.getAllByTrainerIdAndTraining_UserIsNull(this.testTrainer.getId()))
                .thenReturn(List.of(this.testTrainerTraining, this.testTrainerTraining2));
        List<TrainingBasicInfoServiceModel> actual = this.trainerTrainingService.getReadyTrainings(this.testTrainer.getId());
        Assertions.assertEquals(2, actual.size(), "TrainerTraining is not correct");
    }

    @Test
    void getReadyTrainingsShouldReturnCorrectZeroSize() {
        Mockito.when(
                this.trainerTrainingRepository.getAllByTrainerIdAndTraining_UserIsNull(this.testTrainer.getId()))
                .thenReturn(List.of());
        List<TrainingBasicInfoServiceModel> actual = this.trainerTrainingService.getReadyTrainings(this.testTrainer.getId());
        Assertions.assertEquals(0, actual.size(), "TrainerTraining is not correct");
    }

    @Test
    void getReadyTrainingsShouldReturnCorrectTitle() {
        Mockito.when(
                this.trainerTrainingRepository.getAllByTrainerIdAndTraining_UserIsNull(this.testTrainer.getId()))
                .thenReturn(List.of(this.testTrainerTraining, this.testTrainerTraining2));
        List<TrainingBasicInfoServiceModel> actual = this.trainerTrainingService.getReadyTrainings(this.testTrainer.getId());
        Assertions.assertEquals(this.testTraining.getTitle(), actual.get(0).getTitle(), "TrainerTraining is not correct");
    }

    @Test
    void getReadyTrainingsShouldReturnCorrectDifficultyType() {
        Mockito.when(
                this.trainerTrainingRepository.getAllByTrainerIdAndTraining_UserIsNull(this.testTrainer.getId()))
                .thenReturn(List.of(this.testTrainerTraining, this.testTrainerTraining2));
        List<TrainingBasicInfoServiceModel> actual = this.trainerTrainingService.getReadyTrainings(this.testTrainer.getId());
        Assertions.assertEquals(this.testTraining.getDifficulty(), actual.get(0).getDifficulty(), "TrainerTraining is not correct");
    }

    @Test
    void getReadyTrainingsShouldReturnCorrectId() {
        Mockito.when(
                this.trainerTrainingRepository.getAllByTrainerIdAndTraining_UserIsNull(this.testTrainer.getId()))
                .thenReturn(List.of(this.testTrainerTraining, this.testTrainerTraining2));
        List<TrainingBasicInfoServiceModel> actual = this.trainerTrainingService.getReadyTrainings(this.testTrainer.getId());
        Assertions.assertEquals(this.testTraining.getId(), actual.get(0).getId(), "TrainerTraining is not correct");
    }

    @Test
    void getReadyTrainingsShouldReturnCorrectTrainingType() {
        Mockito.when(
                this.trainerTrainingRepository.getAllByTrainerIdAndTraining_UserIsNull(this.testTrainer.getId()))
                .thenReturn(List.of(this.testTrainerTraining, this.testTrainerTraining2));
        List<TrainingBasicInfoServiceModel> actual = this.trainerTrainingService.getReadyTrainings(this.testTrainer.getId());
        Assertions.assertEquals(this.testTraining.getTrainingType(), actual.get(0).getTrainingType(), "TrainerTraining is not correct");
    }

    /**
     * TrainerTrainingService - editByTrainerAndTrainingById(...) TESTS
     */

    @Test
    void editByTrainerAndTrainingByIdShouldReturnCorrectId() {
        Mockito.when(
                this.trainerTrainingRepository.getByTrainerIdAndTrainingIdAndTraining_UserIsNull(this.testTrainer.getId(), this.testTraining.getId()))
                .thenReturn(Optional.of(this.testTrainerTraining));
        TrainingEditBindingModel model = new TrainingEditBindingModel();
        ReadyTrainingServiceModel actualModel = new ReadyTrainingServiceModel();
        actualModel.setId(5L);
        Mockito.when(
                this.trainingService.editReadyTrainingById(this.testTraining.getId(), model))
                .thenReturn(actualModel);
        ReadyTrainingServiceModel actual = this.trainerTrainingService
                .editByTrainerAndTrainingById(this.testTrainer.getId(), this.testTraining.getId(), model);
        Assertions.assertEquals(actualModel.getId(), actual.getId(), "TrainerTraining is not correct");
    }

    @Test
    void editByTrainerAndTrainingByIdShouldReturnCorrectTrainingType() {
        Mockito.when(
                this.trainerTrainingRepository.getByTrainerIdAndTrainingIdAndTraining_UserIsNull(this.testTrainer.getId(), this.testTraining.getId()))
                .thenReturn(Optional.of(this.testTrainerTraining));
        TrainingEditBindingModel model = new TrainingEditBindingModel();
        ReadyTrainingServiceModel actualModel = new ReadyTrainingServiceModel();
        actualModel.setId(5L);
        Mockito.when(
                this.trainingService.editReadyTrainingById(this.testTraining.getId(), model))
                .thenReturn(actualModel);
        ReadyTrainingServiceModel actual = this.trainerTrainingService
                .editByTrainerAndTrainingById(this.testTrainer.getId(), this.testTraining.getId(), model);
        Assertions.assertEquals(actualModel.getTrainingType(), actual.getTrainingType(), "TrainerTraining is not correct");
    }

    @Test
    void editByTrainerAndTrainingByIdShouldReturnCorrectStatusType() {
        Mockito.when(
                this.trainerTrainingRepository.getByTrainerIdAndTrainingIdAndTraining_UserIsNull(this.testTrainer.getId(), this.testTraining.getId()))
                .thenReturn(Optional.of(this.testTrainerTraining));
        TrainingEditBindingModel model = new TrainingEditBindingModel();
        ReadyTrainingServiceModel actualModel = new ReadyTrainingServiceModel();
        actualModel.setId(5L);
        Mockito.when(
                this.trainingService.editReadyTrainingById(this.testTraining.getId(), model))
                .thenReturn(actualModel);
        ReadyTrainingServiceModel actual = this.trainerTrainingService
                .editByTrainerAndTrainingById(this.testTrainer.getId(), this.testTraining.getId(), model);
        Assertions.assertEquals(actualModel.getStatusType(), actual.getStatusType(), "TrainerTraining is not correct");
    }

    @Test
    void editByTrainerAndTrainingByIdShouldReturnCorrectDescription() {
        Mockito.when(
                this.trainerTrainingRepository.getByTrainerIdAndTrainingIdAndTraining_UserIsNull(this.testTrainer.getId(), this.testTraining.getId()))
                .thenReturn(Optional.of(this.testTrainerTraining));
        TrainingEditBindingModel model = new TrainingEditBindingModel();
        ReadyTrainingServiceModel actualModel = new ReadyTrainingServiceModel();
        actualModel.setId(5L);
        Mockito.when(
                this.trainingService.editReadyTrainingById(this.testTraining.getId(), model))
                .thenReturn(actualModel);
        ReadyTrainingServiceModel actual = this.trainerTrainingService
                .editByTrainerAndTrainingById(this.testTrainer.getId(), this.testTraining.getId(), model);
        Assertions.assertEquals(actualModel.getDescription(), actual.getDescription(), "TrainerTraining is not correct");
    }

    @Test
    void editByTrainerAndTrainingByIdShouldReturnCorrectTitle() {
        Mockito.when(
                this.trainerTrainingRepository.getByTrainerIdAndTrainingIdAndTraining_UserIsNull(this.testTrainer.getId(), this.testTraining.getId()))
                .thenReturn(Optional.of(this.testTrainerTraining));
        TrainingEditBindingModel model = new TrainingEditBindingModel();
        ReadyTrainingServiceModel actualModel = new ReadyTrainingServiceModel();
        actualModel.setId(5L);
        Mockito.when(
                this.trainingService.editReadyTrainingById(this.testTraining.getId(), model))
                .thenReturn(actualModel);
        ReadyTrainingServiceModel actual = this.trainerTrainingService
                .editByTrainerAndTrainingById(this.testTrainer.getId(), this.testTraining.getId(), model);
        Assertions.assertEquals(actualModel.getTitle(), actual.getTitle(), "TrainerTraining is not correct");
    }

    @Test
    void editByTrainerAndTrainingByIdShouldThrowException() {
        Mockito.when(
                this.trainerTrainingRepository.getByTrainerIdAndTrainingId(this.testTrainer.getId(), this.testTraining.getId()))
                .thenReturn(Optional.of(this.testTrainerTraining));
        TrainingEditBindingModel model = new TrainingEditBindingModel();
        Assertions.assertThrows(NotFoundException.class,
                () -> this.trainerTrainingService
                        .editByTrainerAndTrainingById(this.testTrainer2.getId(), this.testTraining.getId(), model),
                "TrainerTraining is not correct");
    }

    /**
     * TrainerTrainingService - getReadyTrainingById(...) TESTS
     */

    @Test
    void getReadyTrainingByIdShouldReturnCorrectId() {
        Mockito.when(
                this.trainerTrainingRepository.getByTrainerIdAndTrainingId(this.testTrainer.getId(), this.testTraining.getId()))
                .thenReturn(Optional.of(this.testTrainerTraining));
        TrainingFullInfoServiceModel actual = this.trainerTrainingService
                .getReadyTrainingById(this.testTrainer.getId(), this.testTraining.getId());
        Assertions.assertEquals(this.testTraining.getId(), actual.getTraining().getId(), "TrainerTraining is not correct");
    }

    @Test
    void getReadyTrainingByIdShouldReturnCorrectTitle() {
        Mockito.when(
                this.trainerTrainingRepository.getByTrainerIdAndTrainingId(this.testTrainer.getId(), this.testTraining.getId()))
                .thenReturn(Optional.of(this.testTrainerTraining));
        TrainingFullInfoServiceModel actual = this.trainerTrainingService
                .getReadyTrainingById(this.testTrainer.getId(), this.testTraining.getId());
        Assertions.assertEquals(this.testTraining.getTitle(), actual.getTraining().getTitle(), "TrainerTraining is not correct");
    }

    @Test
    void getReadyTrainingByIdShouldReturnCorrectStatusType() {
        Mockito.when(
                this.trainerTrainingRepository.getByTrainerIdAndTrainingId(this.testTrainer.getId(), this.testTraining.getId()))
                .thenReturn(Optional.of(this.testTrainerTraining));
        TrainingFullInfoServiceModel actual = this.trainerTrainingService
                .getReadyTrainingById(this.testTrainer.getId(), this.testTraining.getId());
        Assertions.assertEquals(this.testTraining.getStatusType(), actual.getTraining().getStatusType(), "TrainerTraining is not correct");
    }

    @Test
    void getReadyTrainingByIdShouldReturnCorrectDescription() {
        Mockito.when(
                this.trainerTrainingRepository.getByTrainerIdAndTrainingId(this.testTrainer.getId(), this.testTraining.getId()))
                .thenReturn(Optional.of(this.testTrainerTraining));
        TrainingFullInfoServiceModel actual = this.trainerTrainingService
                .getReadyTrainingById(this.testTrainer.getId(), this.testTraining.getId());
        Assertions.assertEquals(this.testTraining.getDescription(), actual.getTraining().getDescription(), "TrainerTraining is not correct");
    }

    @Test
    void getReadyTrainingByIdShouldReturnCorrectTrainingType() {
        Mockito.when(
                this.trainerTrainingRepository.getByTrainerIdAndTrainingId(this.testTrainer.getId(), this.testTraining.getId()))
                .thenReturn(Optional.of(this.testTrainerTraining));
        TrainingFullInfoServiceModel actual = this.trainerTrainingService
                .getReadyTrainingById(this.testTrainer.getId(), this.testTraining.getId());
        Assertions.assertEquals(this.testTraining.getTrainingType(), actual.getTraining().getTrainingType(), "TrainerTraining is not correct");
    }

    @Test
    void getReadyTrainingByIdShouldReturnCorrectDaySize() {
        int count = 3;
        for (int i = 0; i < count; i++) {
            Day day = new Day();
            day.setWorkouts(new HashSet<>());
            this.testTraining.getDays().add(day);
        }
        Mockito.when(
                this.trainerTrainingRepository.getByTrainerIdAndTrainingId(this.testTrainer.getId(), this.testTraining.getId()))
                .thenReturn(Optional.of(this.testTrainerTraining));
        TrainingFullInfoServiceModel actual = this.trainerTrainingService
                .getReadyTrainingById(this.testTrainer.getId(), this.testTraining.getId());
        Assertions.assertEquals(count, actual.getDays().size(), "TrainerTraining is not correct");
    }

    @Test
    void getReadyTrainingByIdShouldReturnCorrectZeroDaySize() {
        Mockito.when(
                this.trainerTrainingRepository.getByTrainerIdAndTrainingId(this.testTrainer.getId(), this.testTraining.getId()))
                .thenReturn(Optional.of(this.testTrainerTraining));
        TrainingFullInfoServiceModel actual = this.trainerTrainingService
                .getReadyTrainingById(this.testTrainer.getId(), this.testTraining.getId());
        Assertions.assertEquals(0, actual.getDays().size(), "TrainerTraining is not correct");
    }

    @Test
    void getReadyTrainingByIdShouldReturnCorrectZeroWorkoutSize() {
        Day day = new Day();
        day.setWorkouts(new HashSet<>());
        day.getWorkouts().add(new Workout());
        day.getWorkouts().add(new Workout());
        day.getWorkouts().add(new Workout());
        this.testTraining.getDays().add(day);

        Mockito.when(
                this.trainerTrainingRepository.getByTrainerIdAndTrainingId(this.testTrainer.getId(), this.testTraining.getId()))
                .thenReturn(Optional.of(this.testTrainerTraining));
        TrainingFullInfoServiceModel actual = this.trainerTrainingService
                .getReadyTrainingById(this.testTrainer.getId(), this.testTraining.getId());
        Assertions.assertEquals(day.getWorkouts().size(), actual.getDays().get(0).getWorkouts().size(), "TrainerTraining is not correct");
    }

    @Test
    void getReadyTrainingByIdShouldReturnCorrectZeroWorkoutCount() {
        Day day = new Day();
        day.setWorkouts(new HashSet<>());
        day.getWorkouts().add(new Workout());
        day.getWorkouts().add(new Workout());
        day.getWorkouts().add(new Workout());
        this.testTraining.getDays().add(day);

        Mockito.when(
                this.trainerTrainingRepository.getByTrainerIdAndTrainingId(this.testTrainer.getId(), this.testTraining.getId()))
                .thenReturn(Optional.of(this.testTrainerTraining));
        TrainingFullInfoServiceModel actual = this.trainerTrainingService
                .getReadyTrainingById(this.testTrainer.getId(), this.testTraining.getId());
        Assertions.assertEquals(day.getWorkouts().size(), actual.getDays().get(0).getWorkoutsCount(), "TrainerTraining is not correct");
    }

    @Test
    void getReadyTrainingByIdShouldThrowException() {
        Mockito.when(
                this.trainerTrainingRepository.getByTrainerIdAndTrainingId(this.testTrainer.getId(), this.testTraining.getId()))
                .thenReturn(Optional.of(this.testTrainerTraining));

        Assertions.assertThrows(NotFoundException.class,
                () -> this.trainerTrainingService
                        .getReadyTrainingById(this.testTrainer2.getId(), this.testTraining.getId()),
                "TrainerTraining is not correct");
    }
}
