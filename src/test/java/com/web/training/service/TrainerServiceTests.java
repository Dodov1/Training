package com.web.training.service;

import com.web.training.models.exceptionModels.NoUserFoundForTrainerException;
import com.web.training.models.exceptionModels.NotFoundException;
import com.web.training.models.exceptionModels.UserIsAlreadyTrainerException;
import com.web.training.models.bindingModels.*;
import com.web.training.models.entities.Authority;
import com.web.training.models.entities.Picture;
import com.web.training.models.entities.Trainer;
import com.web.training.models.entities.User;
import com.web.training.models.enums.*;
import com.web.training.models.serviceModels.*;
import com.web.training.repositories.AuthorityRepository;
import com.web.training.repositories.TrainerRepository;
import com.web.training.service.impl.TrainerServiceImpl;
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

import static com.web.training.config.appConstants.AppConstants.DEFAULT_TRAINER_AUTHORITY;


@SpringBootTest
public class TrainerServiceTests {

    @Mock
    private TrainerRepository trainerRepository;
    @Mock
    private AuthorityRepository authorityRepository;

    private TrainerService trainerService;
    @Mock
    private TrainingService trainingService;
    @Mock
    private UserTrainerService userTrainerService;
    @Mock
    private TrainerTrainingService trainerTrainingService;


    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private final ModelMapper modelMapper = new ModelMapper();

    private static final Long CORRECT_TRAINER_ID = 1L;
    private static final Long WRONG_TRAINER_ID = 555L;
    private static final String CORRECT_USER_PIC_LOCATION_ONE = "asd1";
    private static final String CORRECT_USER_PIC_LOCATION_TWO = "asd2";
    private static final String WRONG_USER_USERNAME = "mmmmmmm";

    private Trainer testTrainer;
    private Trainer testTrainer2;
    private User testUser;
    private User testUser2;
    private SearchSuggestionBindingModel model;

    @BeforeEach
    public void init() {

        Picture a = new Picture();
        a.setLocation(CORRECT_USER_PIC_LOCATION_TWO);

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
            setId(CORRECT_TRAINER_ID);
            setFromDate(LocalDate.now());
            setType(TrainerType.Group);
            setPhoneNumber("+359887473527");
            setTrainings(new HashSet<>());
            setUser(testUser);
            setStatus(RelationStatus.Accepted);
        }};
        Picture b = new Picture();
        b.setLocation(CORRECT_USER_PIC_LOCATION_ONE);
        this.testUser2 = new User() {{
            setId(23214L);
            setEnabled(true);
            setAuthorities(new ArrayList<>());
            setTrainer(testTrainer2);
            setTrainers(new HashSet<>());
            setTrainings(new HashSet<>());
            setWeights(new HashSet<>());
            setFirstName("PeshoF222");
            setPicture(b);
            setLastName("PeshoL222");
            setUsername("PeshoU222");
            setEmail("Pesho222@mail.bg");
            setAge(18);
            setPassword(passwordEncoder.encode("asdfg"));
            setHeight(184);
        }};
        this.testUser2.getAuthorities().add(new Authority());
        this.testTrainer2 = new Trainer() {{
            setId(23422L);
            setTrainings(new HashSet<>());
            setPhoneNumber("sdf");
            setType(TrainerType.Group);
            setUser(testUser2);
        }};
        this.trainerService = new TrainerServiceImpl(
                this.trainerRepository, this.userTrainerService, this.trainerTrainingService,
                this.trainingService, this.authorityRepository, this.modelMapper
        );
    }

    private void initAllTrainers() {
        List<Long> notIds = new ArrayList<>();
        notIds.add(this.testUser.getId());
        Mockito.when(
                this.trainerRepository.getTop5ByUser_UsernameContainingAndIdNotInAndStatus(this.testTrainer.getUser().getUsername(), notIds, RelationStatus.Accepted)
        ).thenReturn(List.of(this.testTrainer, this.testTrainer2));
        this.model = new SearchSuggestionBindingModel();
        model.setNotIds(notIds);
    }

    /**
     * TrainerService - getTrainerById(...) TESTS
     */

    @Test
    void getTrainerByIdShouldReturnCorrectId() {
        Mockito.when(
                this.trainerRepository.getByIdAndStatus(CORRECT_TRAINER_ID, RelationStatus.Accepted))
                .thenReturn(Optional.of(this.testTrainer));
        TrainerServiceModel actual = this.trainerService.getTrainerById(this.testTrainer.getId());
        Assertions.assertEquals(this.testTrainer.getId(), actual.getId(), "Trainer  is not correct");
    }

    @Test
    void getTrainerByIdShouldReturnCorrectFirstName() {
        Mockito.when(
                this.trainerRepository.getByIdAndStatus(CORRECT_TRAINER_ID, RelationStatus.Accepted))
                .thenReturn(Optional.of(this.testTrainer));
        TrainerServiceModel actual = this.trainerService.getTrainerById(this.testTrainer.getId());
        Assertions.assertEquals(this.testTrainer.getUser().getFirstName(), actual.getFirstName(), "Trainer  is not correct");
    }

    @Test
    void getTrainerByIdShouldReturnCorrectLastName() {
        Mockito.when(
                this.trainerRepository.getByIdAndStatus(CORRECT_TRAINER_ID, RelationStatus.Accepted))
                .thenReturn(Optional.of(this.testTrainer));
        TrainerServiceModel actual = this.trainerService.getTrainerById(this.testTrainer.getId());
        Assertions.assertEquals(this.testTrainer.getUser().getLastName(), actual.getLastName(), "Trainer  is not correct");
    }

    @Test
    void getTrainerByIdShouldReturnCorrectUsername() {
        Mockito.when(
                this.trainerRepository.getByIdAndStatus(CORRECT_TRAINER_ID, RelationStatus.Accepted))
                .thenReturn(Optional.of(this.testTrainer));
        TrainerServiceModel actual = this.trainerService.getTrainerById(this.testTrainer.getId());
        Assertions.assertEquals(this.testTrainer.getUser().getUsername(), actual.getUsername(), "Trainer  is not correct");
    }

    @Test
    void getTrainerByIdShouldReturnCorrectType() {
        Mockito.when(
                this.trainerRepository.getByIdAndStatus(CORRECT_TRAINER_ID, RelationStatus.Accepted))
                .thenReturn(Optional.of(this.testTrainer));
        TrainerServiceModel actual = this.trainerService.getTrainerById(this.testTrainer.getId());
        Assertions.assertEquals(this.testTrainer.getType(), actual.getType(), "Trainer  is not correct");
    }

    @Test
    void getTrainerByIdShouldThrowException() {
        Mockito.when(
                this.trainerRepository.getByIdAndStatus(CORRECT_TRAINER_ID, RelationStatus.Accepted))
                .thenReturn(Optional.of(this.testTrainer));
        Assertions.assertThrows(NotFoundException.class,
                () -> this.trainerService.getTrainerById(WRONG_TRAINER_ID), "Trainer  is not correct");
    }

    /**
     * TrainerService - getNotApprovedTrainers(...) TESTS
     */

    @Test
    void getNotApprovedTrainersShouldReturnCorrectSize() {
        Mockito.when(
                this.trainerRepository.getAllByStatus(RelationStatus.NotAnswered))
                .thenReturn(List.of(this.testTrainer, this.testTrainer2));
        List<TrainerServiceModel> actual = this.trainerService.getNotApprovedTrainers();
        Assertions.assertEquals(2, actual.size(), "Trainer  is not correct");
    }

    @Test
    void getNotApprovedTrainersShouldReturnCorrectId() {
        Mockito.when(
                this.trainerRepository.getAllByStatus(RelationStatus.NotAnswered))
                .thenReturn(List.of(this.testTrainer, this.testTrainer2));
        List<TrainerServiceModel> actual = this.trainerService.getNotApprovedTrainers();
        Assertions.assertEquals(this.testTrainer2.getId(), actual.get(1).getId(), "Trainer  is not correct");
    }

    @Test
    void getNotApprovedTrainersShouldReturnCorrectUsername() {
        Mockito.when(
                this.trainerRepository.getAllByStatus(RelationStatus.NotAnswered))
                .thenReturn(List.of(this.testTrainer, this.testTrainer2));
        List<TrainerServiceModel> actual = this.trainerService.getNotApprovedTrainers();
        Assertions.assertEquals(this.testTrainer2.getUser().getUsername(), actual.get(1).getUsername(), "Trainer  is not correct");
    }

    @Test
    void getNotApprovedTrainersShouldReturnCorrectLastName() {
        Mockito.when(
                this.trainerRepository.getAllByStatus(RelationStatus.NotAnswered))
                .thenReturn(List.of(this.testTrainer, this.testTrainer2));
        List<TrainerServiceModel> actual = this.trainerService.getNotApprovedTrainers();
        Assertions.assertEquals(this.testTrainer2.getUser().getLastName(), actual.get(1).getLastName(), "Trainer  is not correct");
    }

    @Test
    void getNotApprovedTrainersShouldReturnCorrectFirstName() {
        Mockito.when(
                this.trainerRepository.getAllByStatus(RelationStatus.NotAnswered))
                .thenReturn(List.of(this.testTrainer, this.testTrainer2));
        List<TrainerServiceModel> actual = this.trainerService.getNotApprovedTrainers();
        Assertions.assertEquals(this.testTrainer2.getUser().getFirstName(), actual.get(1).getFirstName(), "Trainer  is not correct");
    }

    @Test
    void getNotApprovedTrainersShouldReturnCorrectType() {
        Mockito.when(
                this.trainerRepository.getAllByStatus(RelationStatus.NotAnswered))
                .thenReturn(List.of(this.testTrainer, this.testTrainer2));
        List<TrainerServiceModel> actual = this.trainerService.getNotApprovedTrainers();
        Assertions.assertEquals(this.testTrainer2.getType(), actual.get(1).getType(), "Trainer  is not correct");
    }

    @Test
    void getNotApprovedTrainersShouldReturnCorrectZeroSize() {
        Mockito.when(
                this.trainerRepository.getAllByStatus(RelationStatus.NotAnswered))
                .thenReturn(List.of());
        List<TrainerServiceModel> actual = this.trainerService.getNotApprovedTrainers();
        Assertions.assertEquals(0, actual.size(), "Trainer  is not correct");
    }

    /**
     * TrainerService - getTrainerInfoByUsername(...) TESTS
     */

    @Test
    void getTrainerInfoByUsernameShouldReturnCorrectId() {
        Mockito.when(
                this.trainerRepository.getByUser_UsernameAndStatus(this.testTrainer.getUser().getUsername(), RelationStatus.Accepted))
                .thenReturn(Optional.of(this.testTrainer));
        Mockito.when(
                this.userTrainerService.getUsersForTrainerCount(this.testTrainer.getId()))
                .thenReturn(5);
        Mockito.when(
                this.userTrainerService.getRatingForTrainerId(this.testTrainer.getId()))
                .thenReturn(1.0);
        TrainerFullInfoServiceModel actual = this.trainerService.getTrainerInfoByUsername(this.testTrainer.getUser().getUsername());
        Assertions.assertEquals(this.testTrainer.getId(), actual.getId(), "Trainer  is not correct");
    }

    @Test
    void getTrainerInfoByUsernameShouldReturnCorrectUsername() {
        Mockito.when(
                this.trainerRepository.getByUser_UsernameAndStatus(this.testTrainer.getUser().getUsername(), RelationStatus.Accepted))
                .thenReturn(Optional.of(this.testTrainer));
        Mockito.when(
                this.userTrainerService.getUsersForTrainerCount(this.testTrainer.getId()))
                .thenReturn(5);
        Mockito.when(
                this.userTrainerService.getRatingForTrainerId(this.testTrainer.getId()))
                .thenReturn(1.0);
        TrainerFullInfoServiceModel actual = this.trainerService.getTrainerInfoByUsername(this.testTrainer.getUser().getUsername());
        Assertions.assertEquals(this.testTrainer.getUser().getUsername(), actual.getUsername(), "Trainer  is not correct");
    }

    @Test
    void getTrainerInfoByUsernameShouldReturnCorrectFirstName() {
        Mockito.when(
                this.trainerRepository.getByUser_UsernameAndStatus(this.testTrainer.getUser().getUsername(), RelationStatus.Accepted))
                .thenReturn(Optional.of(this.testTrainer));
        Mockito.when(
                this.userTrainerService.getUsersForTrainerCount(this.testTrainer.getId()))
                .thenReturn(5);
        Mockito.when(
                this.userTrainerService.getRatingForTrainerId(this.testTrainer.getId()))
                .thenReturn(1.0);
        TrainerFullInfoServiceModel actual = this.trainerService.getTrainerInfoByUsername(this.testTrainer.getUser().getUsername());
        Assertions.assertEquals(this.testTrainer.getUser().getFirstName(), actual.getFirstName(), "Trainer  is not correct");
    }

    @Test
    void getTrainerInfoByUsernameShouldReturnCorrectLastName() {
        Mockito.when(
                this.trainerRepository.getByUser_UsernameAndStatus(this.testTrainer.getUser().getUsername(), RelationStatus.Accepted))
                .thenReturn(Optional.of(this.testTrainer));
        Mockito.when(
                this.userTrainerService.getUsersForTrainerCount(this.testTrainer.getId()))
                .thenReturn(5);
        Mockito.when(
                this.userTrainerService.getRatingForTrainerId(this.testTrainer.getId()))
                .thenReturn(1.0);
        TrainerFullInfoServiceModel actual = this.trainerService.getTrainerInfoByUsername(this.testTrainer.getUser().getUsername());
        Assertions.assertEquals(this.testTrainer.getUser().getLastName(), actual.getLastName(), "Trainer  is not correct");
    }

    @Test
    void getTrainerInfoByUsernameShouldReturnCorrectPhoneNumber() {
        Mockito.when(
                this.trainerRepository.getByUser_UsernameAndStatus(this.testTrainer.getUser().getUsername(), RelationStatus.Accepted))
                .thenReturn(Optional.of(this.testTrainer));
        Mockito.when(
                this.userTrainerService.getUsersForTrainerCount(this.testTrainer.getId()))
                .thenReturn(5);
        Mockito.when(
                this.userTrainerService.getRatingForTrainerId(this.testTrainer.getId()))
                .thenReturn(1.0);
        TrainerFullInfoServiceModel actual = this.trainerService.getTrainerInfoByUsername(this.testTrainer.getUser().getUsername());
        Assertions.assertEquals(this.testTrainer.getPhoneNumber(), actual.getPhoneNumber(), "Trainer  is not correct");
    }

    @Test
    void getTrainerInfoByUsernameShouldReturnCorrectTotalUsers() {
        Mockito.when(
                this.trainerRepository.getByUser_UsernameAndStatus(this.testTrainer.getUser().getUsername(), RelationStatus.Accepted))
                .thenReturn(Optional.of(this.testTrainer));
        Mockito.when(
                this.userTrainerService.getUsersForTrainerCount(this.testTrainer.getId()))
                .thenReturn(5);
        Mockito.when(
                this.userTrainerService.getRatingForTrainerId(this.testTrainer.getId()))
                .thenReturn(1.0);
        TrainerFullInfoServiceModel actual = this.trainerService.getTrainerInfoByUsername(this.testTrainer.getUser().getUsername());
        Assertions.assertEquals(5, actual.getTotalUsers(), "Trainer  is not correct");
    }

    @Test
    void getTrainerInfoByUsernameShouldReturnCorrectRating() {
        Mockito.when(
                this.trainerRepository.getByUser_UsernameAndStatus(this.testTrainer.getUser().getUsername(), RelationStatus.Accepted))
                .thenReturn(Optional.of(this.testTrainer));
        Mockito.when(
                this.userTrainerService.getUsersForTrainerCount(this.testTrainer.getId()))
                .thenReturn(5);
        Mockito.when(
                this.userTrainerService.getRatingForTrainerId(this.testTrainer.getId()))
                .thenReturn(1.0);
        TrainerFullInfoServiceModel actual = this.trainerService.getTrainerInfoByUsername(this.testTrainer.getUser().getUsername());
        Assertions.assertEquals(1.0, actual.getRating(), "Trainer  is not correct");
    }

    @Test
    void getTrainerInfoByUsernameShouldThrowException() {
        Mockito.when(
                this.trainerRepository.getByUser_UsernameAndStatus(this.testTrainer.getUser().getUsername(), RelationStatus.Accepted))
                .thenReturn(Optional.of(this.testTrainer));
        Assertions.assertThrows(NotFoundException.class,
                () -> this.trainerService.getTrainerInfoByUsername(this.testTrainer2.getUser().getUsername()), "Trainer  is not correct");
    }

    /**
     * TrainerService - getTrainerProfileById(...) TESTS
     */

    @Test
    void getTrainerProfileByIdShouldReturnCorrectId() {
        Mockito.when(
                this.trainerRepository.getByIdAndStatus(this.testTrainer.getId(), RelationStatus.Accepted))
                .thenReturn(Optional.of(this.testTrainer));
        UserProfileServiceModel actual = this.trainerService.getTrainerProfileById(this.testTrainer.getId());
        Assertions.assertEquals(this.testTrainer.getId(), actual.getId(), "Trainer  is not correct");
    }

    @Test
    void getTrainerProfileByIdShouldReturnCorrectEmail() {
        Mockito.when(
                this.trainerRepository.getByIdAndStatus(this.testTrainer.getId(), RelationStatus.Accepted))
                .thenReturn(Optional.of(this.testTrainer));
        UserProfileServiceModel actual = this.trainerService.getTrainerProfileById(this.testTrainer.getId());
        Assertions.assertEquals(this.testTrainer.getUser().getEmail(), actual.getEmail(), "Trainer  is not correct");
    }

    @Test
    void getTrainerProfileByIdShouldReturnCorrectLastName() {
        Mockito.when(
                this.trainerRepository.getByIdAndStatus(this.testTrainer.getId(), RelationStatus.Accepted))
                .thenReturn(Optional.of(this.testTrainer));
        UserProfileServiceModel actual = this.trainerService.getTrainerProfileById(this.testTrainer.getId());
        Assertions.assertEquals(this.testTrainer.getUser().getLastName(), actual.getLastName(), "Trainer  is not correct");
    }

    @Test
    void getTrainerProfileByIdShouldReturnCorrectFirstName() {
        Mockito.when(
                this.trainerRepository.getByIdAndStatus(this.testTrainer.getId(), RelationStatus.Accepted))
                .thenReturn(Optional.of(this.testTrainer));
        UserProfileServiceModel actual = this.trainerService.getTrainerProfileById(this.testTrainer.getId());
        Assertions.assertEquals(this.testTrainer.getUser().getFirstName(), actual.getFirstName(), "Trainer  is not correct");
    }

    @Test
    void getTrainerProfileByIdShouldReturnCorrectUsername() {
        Mockito.when(
                this.trainerRepository.getByIdAndStatus(this.testTrainer.getId(), RelationStatus.Accepted))
                .thenReturn(Optional.of(this.testTrainer));
        UserProfileServiceModel actual = this.trainerService.getTrainerProfileById(this.testTrainer.getId());
        Assertions.assertEquals(this.testTrainer.getUser().getUsername(), actual.getUsername(), "Trainer  is not correct");
    }

    @Test
    void getTrainerProfileByIdShouldReturnCorrectAge() {
        Mockito.when(
                this.trainerRepository.getByIdAndStatus(this.testTrainer.getId(), RelationStatus.Accepted))
                .thenReturn(Optional.of(this.testTrainer));
        UserProfileServiceModel actual = this.trainerService.getTrainerProfileById(this.testTrainer.getId());
        Assertions.assertEquals(this.testTrainer.getUser().getAge(), actual.getAge(), "Trainer  is not correct");
    }

    @Test
    void getTrainerProfileByIdShouldReturnCorrectHeight() {
        Mockito.when(
                this.trainerRepository.getByIdAndStatus(this.testTrainer.getId(), RelationStatus.Accepted))
                .thenReturn(Optional.of(this.testTrainer));
        UserProfileServiceModel actual = this.trainerService.getTrainerProfileById(this.testTrainer.getId());
        Assertions.assertEquals(this.testTrainer.getUser().getHeight(), actual.getHeight(), "Trainer  is not correct");
    }

    @Test
    void getTrainerProfileByIdShouldReturnCorrectStatusType() {
        Mockito.when(
                this.trainerRepository.getByIdAndStatus(this.testTrainer.getId(), RelationStatus.Accepted))
                .thenReturn(Optional.of(this.testTrainer));
        UserProfileServiceModel actual = this.trainerService.getTrainerProfileById(this.testTrainer.getId());
        Assertions.assertNull(actual.getStatusType(), "Trainer  is not correct");
    }

    /**
     * TrainerService - respondToTrainerRequest(...) TESTS
     */

    @Test
    void respondToTrainerRequestShouldReturnCorrectAcceptedStatus() {
        this.testTrainer.setStatus(RelationStatus.NotAnswered);
        Mockito.when(
                this.trainerRepository.getByIdAndStatus(this.testTrainer.getId(), RelationStatus.NotAnswered))
                .thenReturn(Optional.of(this.testTrainer));
        RespondTrainerRequestBindingModel model = new RespondTrainerRequestBindingModel();
        model.setRelationStatus(RelationStatus.Accepted);
        this.trainerService.respondToTrainerRequest(this.testTrainer.getId(), model);
        Assertions.assertEquals(RelationStatus.Accepted, this.testTrainer.getStatus(), "Trainer  is not correct");
    }

    @Test
    void respondToTrainerRequestShouldReturnCorrectRejectedStatus() {
        this.testTrainer.setStatus(RelationStatus.NotAnswered);
        Mockito.when(
                this.trainerRepository.getByIdAndStatus(this.testTrainer.getId(), RelationStatus.NotAnswered))
                .thenReturn(Optional.of(this.testTrainer));
        RespondTrainerRequestBindingModel model = new RespondTrainerRequestBindingModel();
        model.setRelationStatus(RelationStatus.Rejected);
        this.trainerService.respondToTrainerRequest(this.testTrainer.getId(), model);
        Assertions.assertEquals(RelationStatus.Rejected, this.testTrainer.getStatus(), "Trainer  is not correct");
    }

    @Test
    void respondToTrainerRequestShouldReturnCorrectAuthoritySize() {
        this.testTrainer.setStatus(RelationStatus.NotAnswered);
        Mockito.when(
                this.trainerRepository.getByIdAndStatus(this.testTrainer.getId(), RelationStatus.NotAnswered))
                .thenReturn(Optional.of(this.testTrainer));
        RespondTrainerRequestBindingModel model = new RespondTrainerRequestBindingModel();
        model.setRelationStatus(RelationStatus.Rejected);
        this.trainerService.respondToTrainerRequest(this.testTrainer.getId(), model);
        Assertions.assertEquals(2, this.testTrainer.getUser().getAuthorities().size(), "Trainer  is not correct");
    }

    @Test
    void respondToTrainerRequestShouldReturnCorrectAuthorityId() {
        this.testTrainer.setStatus(RelationStatus.NotAnswered);
        Mockito.when(
                this.trainerRepository.getByIdAndStatus(this.testTrainer.getId(), RelationStatus.NotAnswered))
                .thenReturn(Optional.of(this.testTrainer));
        Authority authority = new Authority();
        authority.setId(65L);
        Mockito.when(
                this.authorityRepository.saveAndFlush(Mockito.any(Authority.class))
        ).thenReturn(authority);
        RespondTrainerRequestBindingModel model = new RespondTrainerRequestBindingModel();
        model.setRelationStatus(RelationStatus.Rejected);
        this.trainerService.respondToTrainerRequest(this.testTrainer.getId(), model);
        Assertions.assertEquals(authority.getId(), this.testTrainer.getUser().getAuthorities().get(1).getId(), "Trainer  is not correct");
    }

    @Test
    void respondToTrainerRequestShouldReturnCorrectAuthorityName() {
        this.testTrainer.setStatus(RelationStatus.NotAnswered);
        Mockito.when(
                this.trainerRepository.getByIdAndStatus(this.testTrainer.getId(), RelationStatus.NotAnswered))
                .thenReturn(Optional.of(this.testTrainer));
        Authority authority = new Authority();
        authority.setName(DEFAULT_TRAINER_AUTHORITY);
        Mockito.when(
                this.authorityRepository.saveAndFlush(Mockito.any(Authority.class))
        ).thenReturn(authority);
        RespondTrainerRequestBindingModel model = new RespondTrainerRequestBindingModel();
        model.setRelationStatus(RelationStatus.Rejected);
        this.trainerService.respondToTrainerRequest(this.testTrainer.getId(), model);
        Assertions.assertEquals(authority.getName(), this.testTrainer.getUser().getAuthorities().get(1).getName(), "Trainer  is not correct");
    }

    @Test
    void respondToTrainerRequestShouldReturnCorrectAuthorityUser() {
        this.testTrainer.setStatus(RelationStatus.NotAnswered);
        Mockito.when(
                this.trainerRepository.getByIdAndStatus(this.testTrainer.getId(), RelationStatus.NotAnswered))
                .thenReturn(Optional.of(this.testTrainer));
        Authority authority = new Authority();
        authority.setUser(this.testTrainer.getUser());
        Mockito.when(
                this.authorityRepository.saveAndFlush(Mockito.any(Authority.class))
        ).thenReturn(authority);
        RespondTrainerRequestBindingModel model = new RespondTrainerRequestBindingModel();
        model.setRelationStatus(RelationStatus.Rejected);
        this.trainerService.respondToTrainerRequest(this.testTrainer.getId(), model);
        Assertions.assertEquals(this.testTrainer.getUser(), this.testTrainer.getUser().getAuthorities().get(1).getUser(), "Trainer  is not correct");
    }

    @Test
    void respondToTrainerRequestShouldThrowException() {
        Mockito.when(
                this.trainerRepository.getByIdAndStatus(this.testTrainer.getId(), RelationStatus.NotAnswered))
                .thenReturn(Optional.of(this.testTrainer));
        RespondTrainerRequestBindingModel model = new RespondTrainerRequestBindingModel();
        model.setRelationStatus(RelationStatus.Rejected);
        ;
        Assertions.assertThrows(
                NotFoundException.class,
                () -> this.trainerService.respondToTrainerRequest(this.testTrainer2.getId(), model), "Trainer  is not correct"
        );
    }

    /**
     * TrainerService - addTrainer(...) TESTS
     */

    @Test
    void addTrainerShouldSetCorrectTrainerType() throws UserIsAlreadyTrainerException {
        this.testTrainer.setStatus(RelationStatus.NotAnswered);
        this.testTrainer.setType(null);
        this.testTrainer.setPhoneNumber(null);
        Mockito.when(
                this.trainerRepository.getByUser(this.testTrainer.getUser()))
                .thenReturn(Optional.of(this.testTrainer));
        AddTrainerRequestBindingModel model = new AddTrainerRequestBindingModel();
        model.setTrainerType(TrainerType.Individual);
        model.setPhoneNumber("demo1");
        this.trainerService.addTrainer(this.testTrainer.getUser(), model);
        Assertions.assertEquals(TrainerType.Individual, this.testTrainer.getType(), "Trainer  is not correct");
    }

    @Test
    void addTrainerShouldSetCorrectPhoneNumber() throws UserIsAlreadyTrainerException {
        this.testTrainer.setStatus(RelationStatus.NotAnswered);
        this.testTrainer.setType(null);
        this.testTrainer.setPhoneNumber(null);
        Mockito.when(
                this.trainerRepository.getByUser(this.testTrainer.getUser()))
                .thenReturn(Optional.of(this.testTrainer));
        AddTrainerRequestBindingModel model = new AddTrainerRequestBindingModel();
        model.setTrainerType(TrainerType.Individual);
        model.setPhoneNumber("demo1");
        this.trainerService.addTrainer(this.testTrainer.getUser(), model);
        Assertions.assertEquals("demo1", this.testTrainer.getPhoneNumber(), "Trainer  is not correct");
    }

    @Test
    void addTrainerShouldSetCorrectStatus() throws UserIsAlreadyTrainerException {
        this.testTrainer.setStatus(RelationStatus.Rejected);
        this.testTrainer.setType(null);
        this.testTrainer.setPhoneNumber(null);
        Mockito.when(
                this.trainerRepository.getByUser(this.testTrainer.getUser()))
                .thenReturn(Optional.of(this.testTrainer));
        AddTrainerRequestBindingModel model = new AddTrainerRequestBindingModel();
        model.setTrainerType(TrainerType.Individual);
        model.setPhoneNumber("demo1");
        this.trainerService.addTrainer(this.testTrainer.getUser(), model);
        Assertions.assertEquals(RelationStatus.NotAnswered, this.testTrainer.getStatus(), "Trainer  is not correct");
    }

    @Test
    void addTrainerShouldSetCorrectFromDate() throws UserIsAlreadyTrainerException {
        this.testTrainer.setStatus(RelationStatus.Rejected);
        this.testTrainer.setType(null);
        this.testTrainer.setPhoneNumber(null);
        Mockito.when(
                this.trainerRepository.getByUser(this.testTrainer.getUser()))
                .thenReturn(Optional.of(this.testTrainer));
        AddTrainerRequestBindingModel model = new AddTrainerRequestBindingModel();
        model.setTrainerType(TrainerType.Individual);
        model.setPhoneNumber("demo1");
        this.trainerService.addTrainer(this.testTrainer.getUser(), model);
        Assertions.assertEquals(LocalDate.now(), this.testTrainer.getFromDate(), "Trainer  is not correct");
    }

    @Test
    void addTrainerShouldNotSetType() throws UserIsAlreadyTrainerException {
        this.testTrainer.setStatus(RelationStatus.Rejected);
        this.testTrainer.setType(null);
        this.testTrainer.setPhoneNumber(null);
        Mockito.when(
                this.trainerRepository.getByUser(this.testTrainer.getUser()))
                .thenReturn(Optional.empty());
        AddTrainerRequestBindingModel model = new AddTrainerRequestBindingModel();
        model.setTrainerType(TrainerType.Individual);
        model.setPhoneNumber("demo1");
        this.trainerService.addTrainer(this.testTrainer.getUser(), model);
        Assertions.assertNull(this.testTrainer.getType(), "Trainer  is not correct");
    }

    @Test
    void addTrainerShouldNotSetStatus() throws UserIsAlreadyTrainerException {
        this.testTrainer.setStatus(null);
        this.testTrainer.setType(null);
        this.testTrainer.setPhoneNumber(null);
        Mockito.when(
                this.trainerRepository.getByUser(this.testTrainer.getUser()))
                .thenReturn(Optional.empty());
        AddTrainerRequestBindingModel model = new AddTrainerRequestBindingModel();
        model.setTrainerType(TrainerType.Individual);
        model.setPhoneNumber("demo1");
        this.trainerService.addTrainer(this.testTrainer.getUser(), model);
        Assertions.assertNull(this.testTrainer.getStatus(), "Trainer  is not correct");
    }

    @Test
    void addTrainerShouldNotSetTrainerType() throws UserIsAlreadyTrainerException {
        this.testTrainer.setStatus(RelationStatus.NotAnswered);
        this.testTrainer.setType(null);
        this.testTrainer.setPhoneNumber(null);
        Mockito.when(
                this.trainerRepository.getByUser(this.testTrainer.getUser()))
                .thenReturn(Optional.empty());
        AddTrainerRequestBindingModel model = new AddTrainerRequestBindingModel();
        model.setTrainerType(TrainerType.Individual);
        model.setPhoneNumber("demo1");
        this.trainerService.addTrainer(this.testTrainer.getUser(), model);
        Assertions.assertNull(this.testTrainer.getType(), "Trainer  is not correct");
    }

    @Test
    void addTrainerShouldNotSetPhoneNumber() throws UserIsAlreadyTrainerException {
        this.testTrainer.setStatus(RelationStatus.NotAnswered);
        this.testTrainer.setType(null);
        this.testTrainer.setPhoneNumber(null);
        Mockito.when(
                this.trainerRepository.getByUser(this.testTrainer.getUser()))
                .thenReturn(Optional.empty());
        AddTrainerRequestBindingModel model = new AddTrainerRequestBindingModel();
        model.setTrainerType(TrainerType.Individual);
        model.setPhoneNumber("demo1");
        this.trainerService.addTrainer(this.testTrainer.getUser(), model);
        Assertions.assertNull(this.testTrainer.getPhoneNumber(), "Trainer  is not correct");
    }

    @Test
    void addTrainerShouldThrowException() {
        this.testTrainer.setStatus(RelationStatus.Accepted);
        this.testTrainer.setType(null);
        this.testTrainer.setPhoneNumber(null);
        Mockito.when(
                this.trainerRepository.getByUser(this.testTrainer.getUser()))
                .thenReturn(Optional.of(this.testTrainer));
        AddTrainerRequestBindingModel model = new AddTrainerRequestBindingModel();
        Assertions.assertThrows(UserIsAlreadyTrainerException.class,
                () -> this.trainerService.addTrainer(this.testTrainer.getUser(), model),
                "Trainer  is not correct");
    }

    /**
     * TrainerService - getSuggestionsByUsername(...) TESTS
     */

    @Test
    void getSuggestionsByUsernameShouldReturnEmptyList() {
        this.initAllTrainers();
        model.setInput(WRONG_USER_USERNAME);
        List<UserBasicPicServiceModel> actual = this.trainerService.getSuggestionsByUsername(model);
        Assertions.assertEquals(0, actual.size(), "Trainer is not correct");
    }

    @Test
    void getSuggestionsByUsernameShouldReturnListWithSizeTwo() {
        this.initAllTrainers();
        model.setInput(this.testTrainer.getUser().getUsername());
        List<UserBasicPicServiceModel> actual = this.trainerService.getSuggestionsByUsername(model);
        Assertions.assertEquals(2, actual.size(), "Trainer is not correct");
    }

    @Test
    void getSuggestionsByUsernameShouldReturnListWithSizeTwoWithNotIdsZeroCount() {
        this.initAllTrainers();
        SearchSuggestionBindingModel model = new SearchSuggestionBindingModel();
        Mockito.when(this.trainerRepository.getTop5ByUser_UsernameContainingAndStatus(this.testUser.getUsername(), RelationStatus.Accepted))
                .thenReturn(List.of(this.testTrainer, this.testTrainer2));
        model.setInput(this.testUser.getUsername());
        model.setNotIds(new ArrayList<>());
        List<UserBasicPicServiceModel> actual = this.trainerService.getSuggestionsByUsername(model);
        Assertions.assertEquals(2, actual.size(), "User is not correct");
    }

    @Test
    void getSuggestionsByWrongUsernameShouldReturnListWithSizeZeroCount() {
        this.initAllTrainers();
        SearchSuggestionBindingModel model = new SearchSuggestionBindingModel();
        Mockito.when(this.trainerRepository.getTop5ByUser_UsernameContainingAndStatus(this.testUser.getUsername(), RelationStatus.Accepted))
                .thenReturn(List.of(this.testTrainer, this.testTrainer2));
        model.setInput(WRONG_USER_USERNAME);
        model.setNotIds(new ArrayList<>());
        List<UserBasicPicServiceModel> actual = this.trainerService.getSuggestionsByUsername(model);
        Assertions.assertEquals(0, actual.size(), "User is not correct");
    }

    @Test
    void getSuggestionsByUsernameShouldReturnListElementOneCorrectId() {
        this.initAllTrainers();
        model.setInput(this.testUser.getUsername());
        List<UserBasicPicServiceModel> actual = this.trainerService.getSuggestionsByUsername(model);
        Assertions.assertEquals(this.testTrainer.getId(), actual.get(0).getId(), "User is not correct");
    }

    @Test
    void getSuggestionsByUsernameShouldReturnListElementOneCorrectUsername() {
        this.initAllTrainers();
        model.setInput(this.testUser.getUsername());
        List<UserBasicPicServiceModel> actual = this.trainerService.getSuggestionsByUsername(model);
        Assertions.assertEquals(this.testTrainer.getUser().getUsername(), actual.get(0).getUsername(), "User is not correct");
    }

    @Test
    void getSuggestionsByUsernameShouldReturnListElementOneCorrectFirstName() {
        this.initAllTrainers();
        model.setInput(this.testUser.getUsername());
        List<UserBasicPicServiceModel> actual = this.trainerService.getSuggestionsByUsername(model);
        Assertions.assertEquals(this.testTrainer.getUser().getFirstName(), actual.get(0).getFirstName(), "User is not correct");
    }

    @Test
    void getSuggestionsByUsernameShouldReturnListElementOneCorrectLastName() {
        this.initAllTrainers();
        model.setInput(this.testUser.getUsername());
        List<UserBasicPicServiceModel> actual = this.trainerService.getSuggestionsByUsername(model);
        Assertions.assertEquals(this.testTrainer.getUser().getLastName(), actual.get(0).getLastName(), "User is not correct");
    }

    @Test
    void getSuggestionsByUsernameShouldReturnListElementOneCorrectProfilePicture() {
        this.initAllTrainers();
        model.setInput(this.testUser.getUsername());
        List<UserBasicPicServiceModel> actual = this.trainerService.getSuggestionsByUsername(model);
        Assertions.assertEquals(this.testTrainer.getUser().getPicture().getLocation(), actual.get(0).getProfilePicture(), "User is not correct");
    }

    /**
     * TrainerService - addTrainingToUser(...) TESTS
     */

    @Test
    void addTrainingToUserShouldSetCorrectTrainingId() throws NoUserFoundForTrainerException {
        Long userId = 61L;
        TrainingServiceModel model = new TrainingServiceModel();
        model.setId(87L);
        TrainingAddBindingModel modelAdd = new TrainingAddBindingModel();
        Mockito.when(
                this.userTrainerService.checkIfTrainerHasUser(this.testTrainer.getId(), userId))
                .thenReturn(true);
        Mockito.when(
                this.trainingService.addNewTraining(userId, modelAdd))
                .thenReturn(model);
        TrainingServiceModel actual = this.trainerService.addTrainingToUser(this.testTrainer.getId(), userId, modelAdd);
        Assertions.assertEquals(model.getId(), actual.getId(), "Trainer  is not correct");
    }

    @Test
    void addTrainingToUserShouldSetCorrectTrainingStatusType() throws NoUserFoundForTrainerException {
        Long userId = 61L;
        TrainingServiceModel model = new TrainingServiceModel();
        model.setStatusType(StatusType.Completed);
        TrainingAddBindingModel modelAdd = new TrainingAddBindingModel();
        Mockito.when(
                this.userTrainerService.checkIfTrainerHasUser(this.testTrainer.getId(), userId))
                .thenReturn(true);
        Mockito.when(
                this.trainingService.addNewTraining(userId, modelAdd))
                .thenReturn(model);
        TrainingServiceModel actual = this.trainerService.addTrainingToUser(this.testTrainer.getId(), userId, modelAdd);
        Assertions.assertEquals(model.getStatusType(), actual.getStatusType(), "Trainer  is not correct");
    }

    @Test
    void addTrainingToUserShouldSetCorrectTrainingFromDate() throws NoUserFoundForTrainerException {
        Long userId = 61L;
        TrainingServiceModel model = new TrainingServiceModel();
        model.setFromDate(LocalDate.MAX);
        TrainingAddBindingModel modelAdd = new TrainingAddBindingModel();
        Mockito.when(
                this.userTrainerService.checkIfTrainerHasUser(this.testTrainer.getId(), userId))
                .thenReturn(true);
        Mockito.when(
                this.trainingService.addNewTraining(userId, modelAdd))
                .thenReturn(model);
        TrainingServiceModel actual = this.trainerService.addTrainingToUser(this.testTrainer.getId(), userId, modelAdd);
        Assertions.assertEquals(model.getFromDate(), actual.getFromDate(), "Trainer  is not correct");
    }

    @Test
    void addTrainingToUserShouldSetCorrectTrainingToDate() throws NoUserFoundForTrainerException {
        Long userId = 61L;
        TrainingServiceModel model = new TrainingServiceModel();
        model.setToDate(LocalDate.MIN);
        TrainingAddBindingModel modelAdd = new TrainingAddBindingModel();
        Mockito.when(
                this.userTrainerService.checkIfTrainerHasUser(this.testTrainer.getId(), userId))
                .thenReturn(true);
        Mockito.when(
                this.trainingService.addNewTraining(userId, modelAdd))
                .thenReturn(model);
        TrainingServiceModel actual = this.trainerService.addTrainingToUser(this.testTrainer.getId(), userId, modelAdd);
        Assertions.assertEquals(model.getToDate(), actual.getToDate(), "Trainer  is not correct");
    }

    @Test
    void addTrainingToUserShouldSetCorrectTrainingTitle() throws NoUserFoundForTrainerException {
        Long userId = 61L;
        TrainingServiceModel model = new TrainingServiceModel();
        model.setTitle("StatusType.Completed");
        TrainingAddBindingModel modelAdd = new TrainingAddBindingModel();
        Mockito.when(
                this.userTrainerService.checkIfTrainerHasUser(this.testTrainer.getId(), userId))
                .thenReturn(true);
        Mockito.when(
                this.trainingService.addNewTraining(userId, modelAdd))
                .thenReturn(model);
        TrainingServiceModel actual = this.trainerService.addTrainingToUser(this.testTrainer.getId(), userId, modelAdd);
        Assertions.assertEquals(model.getTitle(), actual.getTitle(), "Trainer  is not correct");
    }

    @Test
    void addTrainingToUserShouldSetCorrectTrainingDifficultyType() throws NoUserFoundForTrainerException {
        Long userId = 61L;
        TrainingServiceModel model = new TrainingServiceModel();
        model.setDifficulty(DifficultyType.Easy);
        TrainingAddBindingModel modelAdd = new TrainingAddBindingModel();
        Mockito.when(
                this.userTrainerService.checkIfTrainerHasUser(this.testTrainer.getId(), userId))
                .thenReturn(true);
        Mockito.when(
                this.trainingService.addNewTraining(userId, modelAdd))
                .thenReturn(model);
        TrainingServiceModel actual = this.trainerService.addTrainingToUser(this.testTrainer.getId(), userId, modelAdd);
        Assertions.assertEquals(model.getDifficulty(), actual.getDifficulty(), "Trainer  is not correct");
    }

    @Test
    void addTrainingToUserShouldSetCorrectTrainingTrainingType() throws NoUserFoundForTrainerException {
        Long userId = 61L;
        TrainingServiceModel model = new TrainingServiceModel();
        model.setTrainingType(TrainingType.Bike);
        TrainingAddBindingModel modelAdd = new TrainingAddBindingModel();
        Mockito.when(
                this.userTrainerService.checkIfTrainerHasUser(this.testTrainer.getId(), userId))
                .thenReturn(true);
        Mockito.when(
                this.trainingService.addNewTraining(userId, modelAdd))
                .thenReturn(model);
        TrainingServiceModel actual = this.trainerService.addTrainingToUser(this.testTrainer.getId(), userId, modelAdd);
        Assertions.assertEquals(model.getTrainingType(), actual.getTrainingType(), "Trainer  is not correct");
    }

    @Test
    void addTrainingToUserShouldSetCorrectTrainingDescription() throws NoUserFoundForTrainerException {
        Long userId = 61L;
        TrainingServiceModel model = new TrainingServiceModel();
        model.setDescription("StatusType.Completed");
        TrainingAddBindingModel modelAdd = new TrainingAddBindingModel();
        Mockito.when(
                this.userTrainerService.checkIfTrainerHasUser(this.testTrainer.getId(), userId))
                .thenReturn(true);
        Mockito.when(
                this.trainingService.addNewTraining(userId, modelAdd))
                .thenReturn(model);
        TrainingServiceModel actual = this.trainerService.addTrainingToUser(this.testTrainer.getId(), userId, modelAdd);
        Assertions.assertEquals(model.getDescription(), actual.getDescription(), "Trainer  is not correct");
    }

    @Test
    void addTrainingToUserShouldThrowException() {
        Long userId = 61L;
        TrainingAddBindingModel modelAdd = new TrainingAddBindingModel();
        Mockito.when(
                this.userTrainerService.checkIfTrainerHasUser(this.testTrainer.getId(), userId))
                .thenReturn(false);
        Assertions.assertThrows(NoUserFoundForTrainerException.class,
                () -> this.trainerService.addTrainingToUser(this.testTrainer.getId(), userId, modelAdd), "Trainer  is not correct"
        );
    }

    /**
     * TrainerService - editTrainingToUser(...) TESTS
     */

    @Test
    void editTrainingToUserShouldSetCorrectTrainingId() throws NoUserFoundForTrainerException {
        Long trainingId = 61L;
        Long userId = 161L;
        TrainingServiceModel model = new TrainingServiceModel();
        model.setId(87L);
        TrainingEditBindingModel modelAdd = new TrainingEditBindingModel();
        Mockito.when(
                this.userTrainerService.checkIfTrainerHasUser(this.testTrainer.getId(), userId))
                .thenReturn(true);
        Mockito.when(
                this.trainingService.editByTrainingById(userId, trainingId, modelAdd))
                .thenReturn(model);
        TrainingServiceModel actual = this.trainerService.editTrainingToUser(this.testTrainer.getId(), userId, trainingId, modelAdd);
        Assertions.assertEquals(model.getId(), actual.getId(), "Trainer  is not correct");
    }

    @Test
    void editTrainingToUserShouldSetCorrectTrainingStatusType() throws NoUserFoundForTrainerException {
        Long userId = 61L;
        Long trainingId = 161L;
        TrainingServiceModel model = new TrainingServiceModel();
        model.setStatusType(StatusType.Completed);
        TrainingEditBindingModel modelAdd = new TrainingEditBindingModel();
        Mockito.when(
                this.userTrainerService.checkIfTrainerHasUser(this.testTrainer.getId(), userId))
                .thenReturn(true);
        Mockito.when(
                this.trainingService.editByTrainingById(userId, trainingId, modelAdd))
                .thenReturn(model);
        TrainingServiceModel actual = this.trainerService.editTrainingToUser(this.testTrainer.getId(), userId, trainingId, modelAdd);
        Assertions.assertEquals(model.getStatusType(), actual.getStatusType(), "Trainer  is not correct");
    }

    @Test
    void editTrainingToUserShouldSetCorrectTrainingFromDate() throws NoUserFoundForTrainerException {
        Long userId = 61L;
        Long trainingId = 161L;
        TrainingServiceModel model = new TrainingServiceModel();
        model.setFromDate(LocalDate.MAX);
        TrainingEditBindingModel modelAdd = new TrainingEditBindingModel();
        Mockito.when(
                this.userTrainerService.checkIfTrainerHasUser(this.testTrainer.getId(), userId))
                .thenReturn(true);
        Mockito.when(
                this.trainingService.editByTrainingById(userId, trainingId, modelAdd))
                .thenReturn(model);
        TrainingServiceModel actual = this.trainerService.editTrainingToUser(this.testTrainer.getId(), userId, trainingId, modelAdd);
        Assertions.assertEquals(model.getFromDate(), actual.getFromDate(), "Trainer  is not correct");
    }

    @Test
    void editTrainingToUserShouldSetCorrectTrainingToDate() throws NoUserFoundForTrainerException {
        Long userId = 61L;
        Long trainingId = 161L;
        TrainingServiceModel model = new TrainingServiceModel();
        model.setToDate(LocalDate.MIN);
        TrainingEditBindingModel modelAdd = new TrainingEditBindingModel();
        Mockito.when(
                this.userTrainerService.checkIfTrainerHasUser(this.testTrainer.getId(), userId))
                .thenReturn(true);
        Mockito.when(
                this.trainingService.editByTrainingById(userId, trainingId, modelAdd))
                .thenReturn(model);
        TrainingServiceModel actual = this.trainerService.editTrainingToUser(this.testTrainer.getId(), userId, trainingId, modelAdd);
        Assertions.assertEquals(model.getToDate(), actual.getToDate(), "Trainer  is not correct");
    }

    @Test
    void editTrainingToUserShouldSetCorrectTrainingTitle() throws NoUserFoundForTrainerException {
        Long userId = 61L;
        Long trainingId = 161L;
        TrainingServiceModel model = new TrainingServiceModel();
        model.setTitle("StatusType.Completed");
        TrainingEditBindingModel modelAdd = new TrainingEditBindingModel();
        Mockito.when(
                this.userTrainerService.checkIfTrainerHasUser(this.testTrainer.getId(), userId))
                .thenReturn(true);
        Mockito.when(
                this.trainingService.editByTrainingById(userId, trainingId, modelAdd))
                .thenReturn(model);
        TrainingServiceModel actual = this.trainerService.editTrainingToUser(this.testTrainer.getId(), userId, trainingId, modelAdd);
        Assertions.assertEquals(model.getTitle(), actual.getTitle(), "Trainer  is not correct");
    }

    @Test
    void editTrainingToUserShouldSetCorrectTrainingDifficultyType() throws NoUserFoundForTrainerException {
        Long userId = 61L;
        Long trainingId = 161L;
        TrainingServiceModel model = new TrainingServiceModel();
        model.setDifficulty(DifficultyType.Easy);
        TrainingEditBindingModel modelAdd = new TrainingEditBindingModel();
        Mockito.when(
                this.userTrainerService.checkIfTrainerHasUser(this.testTrainer.getId(), userId))
                .thenReturn(true);
        Mockito.when(
                this.trainingService.editByTrainingById(userId, trainingId, modelAdd))
                .thenReturn(model);
        TrainingServiceModel actual = this.trainerService.editTrainingToUser(this.testTrainer.getId(), userId, trainingId, modelAdd);
        Assertions.assertEquals(model.getDifficulty(), actual.getDifficulty(), "Trainer  is not correct");
    }

    @Test
    void editTrainingToUserShouldSetCorrectTrainingTrainingType() throws NoUserFoundForTrainerException {
        Long userId = 61L;
        Long trainingId = 161L;
        TrainingServiceModel model = new TrainingServiceModel();
        model.setTrainingType(TrainingType.Bike);
        TrainingEditBindingModel modelAdd = new TrainingEditBindingModel();
        Mockito.when(
                this.userTrainerService.checkIfTrainerHasUser(this.testTrainer.getId(), userId))
                .thenReturn(true);
        Mockito.when(
                this.trainingService.editByTrainingById(userId, trainingId, modelAdd))
                .thenReturn(model);
        TrainingServiceModel actual = this.trainerService.editTrainingToUser(this.testTrainer.getId(), userId, trainingId, modelAdd);
        Assertions.assertEquals(model.getTrainingType(), actual.getTrainingType(), "Trainer  is not correct");
    }

    @Test
    void editTrainingToUserShouldSetCorrectTrainingDescription() throws NoUserFoundForTrainerException {
        Long userId = 61L;
        Long trainingId = 161L;
        TrainingServiceModel model = new TrainingServiceModel();
        model.setDescription("StatusType.Completed");
        TrainingEditBindingModel modelAdd = new TrainingEditBindingModel();
        Mockito.when(
                this.userTrainerService.checkIfTrainerHasUser(this.testTrainer.getId(), userId))
                .thenReturn(true);
        Mockito.when(
                this.trainingService.editByTrainingById(userId, trainingId, modelAdd))
                .thenReturn(model);
        TrainingServiceModel actual = this.trainerService.editTrainingToUser(this.testTrainer.getId(), userId, trainingId, modelAdd);
        Assertions.assertEquals(model.getDescription(), actual.getDescription(), "Trainer  is not correct");
    }

    @Test
    void editTrainingToUserShouldThrowException() {
        Long userId = 61L;
        Long trainingId = 161L;
        TrainingEditBindingModel modelAdd = new TrainingEditBindingModel();
        Mockito.when(
                this.userTrainerService.checkIfTrainerHasUser(this.testTrainer.getId(), userId))
                .thenReturn(false);
        Assertions.assertThrows(NoUserFoundForTrainerException.class,
                () -> this.trainerService.editTrainingToUser(this.testTrainer.getId(), userId, trainingId, modelAdd), "Trainer  is not correct"
        );
    }

    /**
     * TrainerService - addReadyTraining(...) TESTS
     */

    @Test
    void addReadyTrainingShouldSetCorrectTrainingId() {
        ReadyTrainingServiceModel model = new ReadyTrainingServiceModel();
        model.setId(87L);
        TrainingAddBindingModel modelAdd = new TrainingAddBindingModel();
        Mockito.when(
                this.trainerRepository.getByIdAndStatus(this.testTrainer.getId(), RelationStatus.Accepted))
                .thenReturn(Optional.of(this.testTrainer));
        Mockito.when(
                this.trainingService.addNewReadyTraining(modelAdd))
                .thenReturn(model);
        ReadyTrainingServiceModel actual = this.trainerService.addReadyTraining(this.testTrainer.getId(), modelAdd);
        Assertions.assertEquals(model.getId(), actual.getId(), "Trainer  is not correct");
    }

    @Test
    void addReadyTrainingShouldSetCorrectTrainingStatusType() {
        ReadyTrainingServiceModel model = new ReadyTrainingServiceModel();
        model.setStatusType(StatusType.Completed);
        TrainingAddBindingModel modelAdd = new TrainingAddBindingModel();
        Mockito.when(
                this.trainerRepository.getByIdAndStatus(this.testTrainer.getId(), RelationStatus.Accepted))
                .thenReturn(Optional.of(this.testTrainer));
        Mockito.when(
                this.trainingService.addNewReadyTraining(modelAdd))
                .thenReturn(model);
        ReadyTrainingServiceModel actual = this.trainerService.addReadyTraining(this.testTrainer.getId(), modelAdd);
        Assertions.assertEquals(model.getStatusType(), actual.getStatusType(), "Trainer  is not correct");
    }

    @Test
    void addReadyTrainingShouldSetCorrectTrainingTitle() {
        ReadyTrainingServiceModel model = new ReadyTrainingServiceModel();
        model.setTitle("StatusType.Completed");
        TrainingAddBindingModel modelAdd = new TrainingAddBindingModel();
        Mockito.when(
                this.trainerRepository.getByIdAndStatus(this.testTrainer.getId(), RelationStatus.Accepted))
                .thenReturn(Optional.of(this.testTrainer));
        Mockito.when(
                this.trainingService.addNewReadyTraining(modelAdd))
                .thenReturn(model);
        ReadyTrainingServiceModel actual = this.trainerService.addReadyTraining(this.testTrainer.getId(), modelAdd);
        Assertions.assertEquals(model.getTitle(), actual.getTitle(), "Trainer  is not correct");
    }

    @Test
    void addReadyTrainingShouldSetCorrectTrainingDifficultyType() {
        ReadyTrainingServiceModel model = new ReadyTrainingServiceModel();
        model.setDifficulty(DifficultyType.Easy);
        TrainingAddBindingModel modelAdd = new TrainingAddBindingModel();
        Mockito.when(
                this.trainerRepository.getByIdAndStatus(this.testTrainer.getId(), RelationStatus.Accepted))
                .thenReturn(Optional.of(this.testTrainer));
        Mockito.when(
                this.trainingService.addNewReadyTraining(modelAdd))
                .thenReturn(model);
        ReadyTrainingServiceModel actual = this.trainerService.addReadyTraining(this.testTrainer.getId(), modelAdd);
        Assertions.assertEquals(model.getDifficulty(), actual.getDifficulty(), "Trainer  is not correct");
    }

    @Test
    void addReadyTrainingShouldSetCorrectTrainingTrainingType() {
        ReadyTrainingServiceModel model = new ReadyTrainingServiceModel();
        model.setTrainingType(TrainingType.Bike);
        TrainingAddBindingModel modelAdd = new TrainingAddBindingModel();
        Mockito.when(
                this.trainerRepository.getByIdAndStatus(this.testTrainer.getId(), RelationStatus.Accepted))
                .thenReturn(Optional.of(this.testTrainer));
        Mockito.when(
                this.trainingService.addNewReadyTraining(modelAdd))
                .thenReturn(model);
        ReadyTrainingServiceModel actual = this.trainerService.addReadyTraining(this.testTrainer.getId(), modelAdd);
        Assertions.assertEquals(model.getTrainingType(), actual.getTrainingType(), "Trainer  is not correct");
    }

    @Test
    void addReadyTrainingShouldSetCorrectTrainingDescription() {
        ReadyTrainingServiceModel model = new ReadyTrainingServiceModel();
        model.setDescription("StatusType.Completed");
        TrainingAddBindingModel modelAdd = new TrainingAddBindingModel();
        Mockito.when(
                this.trainerRepository.getByIdAndStatus(this.testTrainer.getId(), RelationStatus.Accepted))
                .thenReturn(Optional.of(this.testTrainer));
        Mockito.when(
                this.trainingService.addNewReadyTraining(modelAdd))
                .thenReturn(model);
        ReadyTrainingServiceModel actual = this.trainerService.addReadyTraining(this.testTrainer.getId(), modelAdd);
        Assertions.assertEquals(model.getDescription(), actual.getDescription(), "Trainer  is not correct");
    }

    @Test
    void addReadyTrainingShouldThrowException() {
        Long userId = 61L;
        TrainingAddBindingModel modelAdd = new TrainingAddBindingModel();
        Mockito.when(
                this.trainerRepository.getByIdAndStatus(this.testTrainer.getId(), RelationStatus.Accepted))
                .thenReturn(Optional.empty());
        Assertions.assertThrows(NoUserFoundForTrainerException.class,
                () -> this.trainerService.addTrainingToUser(this.testTrainer.getId(), userId, modelAdd), "Trainer  is not correct"
        );
    }

    /**
     * TrainerService - addReadyTrainingToUser(...) TESTS
     */

    @Test
    void addReadyTrainingToUserShouldSetCorrectTrainingId() throws NoUserFoundForTrainerException {
        Long userId = 61L;
        Long trainingId = 611L;
        TrainingServiceModel model = new TrainingServiceModel();
        model.setId(87L);
        ReadyTrainingStartBindingModel modelAdd = new ReadyTrainingStartBindingModel();
        modelAdd.setStartDate(LocalDate.now());
        Mockito.when(
                this.userTrainerService.checkIfTrainerHasUser(this.testTrainer.getId(), userId))
                .thenReturn(true);
        Mockito.when(
                this.trainerRepository.getByIdAndStatus(this.testTrainer.getId(), RelationStatus.Accepted))
                .thenReturn(Optional.of(this.testTrainer));
        Mockito.when(
                this.trainingService.addReadyTrainingToUser(trainingId, userId, modelAdd.getStartDate()))
                .thenReturn(model);
        TrainingServiceModel actual = this.trainerService.addReadyTrainingToUser(this.testTrainer.getId(), userId, trainingId, modelAdd);
        Assertions.assertEquals(model.getId(), actual.getId(), "Trainer  is not correct");
    }

    @Test
    void addReadyTrainingToUserShouldSetCorrectTrainingTrainingType() throws NoUserFoundForTrainerException {
        Long userId = 61L;
        Long trainingId = 611L;
        TrainingServiceModel model = new TrainingServiceModel();
        model.setTrainingType(TrainingType.Bike);
        ReadyTrainingStartBindingModel modelAdd = new ReadyTrainingStartBindingModel();
        modelAdd.setStartDate(LocalDate.now());
        Mockito.when(
                this.userTrainerService.checkIfTrainerHasUser(this.testTrainer.getId(), userId))
                .thenReturn(true);
        Mockito.when(
                this.trainerRepository.getByIdAndStatus(this.testTrainer.getId(), RelationStatus.Accepted))
                .thenReturn(Optional.of(this.testTrainer));
        Mockito.when(
                this.trainingService.addReadyTrainingToUser(trainingId, userId, modelAdd.getStartDate()))
                .thenReturn(model);
        TrainingServiceModel actual = this.trainerService.addReadyTrainingToUser(this.testTrainer.getId(), userId, trainingId, modelAdd);
        Assertions.assertEquals(model.getTrainingType(), actual.getTrainingType(), "Trainer  is not correct");
    }

    @Test
    void addReadyTrainingToUserShouldSetCorrectTrainingTitle() throws NoUserFoundForTrainerException {
        Long userId = 61L;
        Long trainingId = 611L;
        TrainingServiceModel model = new TrainingServiceModel();
        model.setTitle("87L");
        ReadyTrainingStartBindingModel modelAdd = new ReadyTrainingStartBindingModel();
        modelAdd.setStartDate(LocalDate.now());
        Mockito.when(
                this.userTrainerService.checkIfTrainerHasUser(this.testTrainer.getId(), userId))
                .thenReturn(true);
        Mockito.when(
                this.trainerRepository.getByIdAndStatus(this.testTrainer.getId(), RelationStatus.Accepted))
                .thenReturn(Optional.of(this.testTrainer));
        Mockito.when(
                this.trainingService.addReadyTrainingToUser(trainingId, userId, modelAdd.getStartDate()))
                .thenReturn(model);
        TrainingServiceModel actual = this.trainerService.addReadyTrainingToUser(this.testTrainer.getId(), userId, trainingId, modelAdd);
        Assertions.assertEquals(model.getTitle(), actual.getTitle(), "Trainer  is not correct");
    }

    @Test
    void addReadyTrainingToUserShouldSetCorrectTrainingStatusType() throws NoUserFoundForTrainerException {
        Long userId = 61L;
        Long trainingId = 611L;
        TrainingServiceModel model = new TrainingServiceModel();
        model.setStatusType(StatusType.Future);
        ReadyTrainingStartBindingModel modelAdd = new ReadyTrainingStartBindingModel();
        modelAdd.setStartDate(LocalDate.now());
        Mockito.when(
                this.userTrainerService.checkIfTrainerHasUser(this.testTrainer.getId(), userId))
                .thenReturn(true);
        Mockito.when(
                this.trainerRepository.getByIdAndStatus(this.testTrainer.getId(), RelationStatus.Accepted))
                .thenReturn(Optional.of(this.testTrainer));
        Mockito.when(
                this.trainingService.addReadyTrainingToUser(trainingId, userId, modelAdd.getStartDate()))
                .thenReturn(model);
        TrainingServiceModel actual = this.trainerService.addReadyTrainingToUser(this.testTrainer.getId(), userId, trainingId, modelAdd);
        Assertions.assertEquals(model.getStatusType(), actual.getStatusType(), "Trainer  is not correct");
    }

    @Test
    void addReadyTrainingToUserShouldSetCorrectTrainingToDate() throws NoUserFoundForTrainerException {
        Long userId = 61L;
        Long trainingId = 611L;
        TrainingServiceModel model = new TrainingServiceModel();
        model.setToDate(LocalDate.EPOCH);
        ReadyTrainingStartBindingModel modelAdd = new ReadyTrainingStartBindingModel();
        modelAdd.setStartDate(LocalDate.now());
        Mockito.when(
                this.userTrainerService.checkIfTrainerHasUser(this.testTrainer.getId(), userId))
                .thenReturn(true);
        Mockito.when(
                this.trainerRepository.getByIdAndStatus(this.testTrainer.getId(), RelationStatus.Accepted))
                .thenReturn(Optional.of(this.testTrainer));
        Mockito.when(
                this.trainingService.addReadyTrainingToUser(trainingId, userId, modelAdd.getStartDate()))
                .thenReturn(model);
        TrainingServiceModel actual = this.trainerService.addReadyTrainingToUser(this.testTrainer.getId(), userId, trainingId, modelAdd);
        Assertions.assertEquals(model.getToDate(), actual.getToDate(), "Trainer  is not correct");
    }

    @Test
    void addReadyTrainingToUserShouldSetCorrectTrainingFromDate() throws NoUserFoundForTrainerException {
        Long userId = 61L;
        Long trainingId = 611L;
        TrainingServiceModel model = new TrainingServiceModel();
        model.setFromDate(LocalDate.MAX);
        ReadyTrainingStartBindingModel modelAdd = new ReadyTrainingStartBindingModel();
        modelAdd.setStartDate(LocalDate.now());
        Mockito.when(
                this.userTrainerService.checkIfTrainerHasUser(this.testTrainer.getId(), userId))
                .thenReturn(true);
        Mockito.when(
                this.trainerRepository.getByIdAndStatus(this.testTrainer.getId(), RelationStatus.Accepted))
                .thenReturn(Optional.of(this.testTrainer));
        Mockito.when(
                this.trainingService.addReadyTrainingToUser(trainingId, userId, modelAdd.getStartDate()))
                .thenReturn(model);
        TrainingServiceModel actual = this.trainerService.addReadyTrainingToUser(this.testTrainer.getId(), userId, trainingId, modelAdd);
        Assertions.assertEquals(model.getFromDate(), actual.getFromDate(), "Trainer  is not correct");
    }

    @Test
    void addReadyTrainingToUserShouldSetCorrectTrainingDescription() throws NoUserFoundForTrainerException {
        Long userId = 61L;
        Long trainingId = 611L;
        TrainingServiceModel model = new TrainingServiceModel();
        model.setDescription("87L");
        ReadyTrainingStartBindingModel modelAdd = new ReadyTrainingStartBindingModel();
        modelAdd.setStartDate(LocalDate.now());
        Mockito.when(
                this.userTrainerService.checkIfTrainerHasUser(this.testTrainer.getId(), userId))
                .thenReturn(true);
        Mockito.when(
                this.trainerRepository.getByIdAndStatus(this.testTrainer.getId(), RelationStatus.Accepted))
                .thenReturn(Optional.of(this.testTrainer));
        Mockito.when(
                this.trainingService.addReadyTrainingToUser(trainingId, userId, modelAdd.getStartDate()))
                .thenReturn(model);
        TrainingServiceModel actual = this.trainerService.addReadyTrainingToUser(this.testTrainer.getId(), userId, trainingId, modelAdd);
        Assertions.assertEquals(model.getDescription(), actual.getDescription(), "Trainer  is not correct");
    }

    @Test
    void addReadyTrainingToUserShouldSetCorrectTrainingDifficulty() throws NoUserFoundForTrainerException {
        Long userId = 61L;
        Long trainingId = 611L;
        TrainingServiceModel model = new TrainingServiceModel();
        model.setDifficulty(DifficultyType.Easy);
        ReadyTrainingStartBindingModel modelAdd = new ReadyTrainingStartBindingModel();
        modelAdd.setStartDate(LocalDate.now());
        Mockito.when(
                this.userTrainerService.checkIfTrainerHasUser(this.testTrainer.getId(), userId))
                .thenReturn(true);
        Mockito.when(
                this.trainerRepository.getByIdAndStatus(this.testTrainer.getId(), RelationStatus.Accepted))
                .thenReturn(Optional.of(this.testTrainer));
        Mockito.when(
                this.trainingService.addReadyTrainingToUser(trainingId, userId, modelAdd.getStartDate()))
                .thenReturn(model);
        TrainingServiceModel actual = this.trainerService.addReadyTrainingToUser(this.testTrainer.getId(), userId, trainingId, modelAdd);
        Assertions.assertEquals(model.getDifficulty(), actual.getDifficulty(), "Trainer  is not correct");
    }

    @Test
    void addReadyTrainingToUserShouldThrowException() {
        Long userId = 61L;
        ReadyTrainingStartBindingModel modelAdd = new ReadyTrainingStartBindingModel();
        Mockito.when(
                this.trainerRepository.getByIdAndStatus(this.testTrainer.getId(), RelationStatus.Accepted))
                .thenReturn(Optional.of(this.testTrainer));
        Mockito.when(
                this.userTrainerService.checkIfTrainerHasUser(this.testTrainer.getId(), userId))
                .thenReturn(false);
        Assertions.assertThrows(NoUserFoundForTrainerException.class,
                () -> this.trainerService.addReadyTrainingToUser(this.testTrainer.getId(), userId, 1L, modelAdd), "Trainer  is not correct"
        );
    }

    @Test
    void addReadyTrainingToUserShouldThrowExceptionTrainerNotFound() {
        Long userId = 61L;
        ReadyTrainingStartBindingModel modelAdd = new ReadyTrainingStartBindingModel();
        Mockito.when(
                this.trainerRepository.getByIdAndStatus(this.testTrainer.getId(), RelationStatus.Accepted))
                .thenReturn(Optional.empty());
        Assertions.assertThrows(NotFoundException.class,
                () -> this.trainerService.addReadyTrainingToUser(this.testTrainer.getId(), userId, 1L, modelAdd), "Trainer  is not correct"
        );
    }

    /**
     * TrainerService - getTrainingWithDayLinks(...) TESTS
     */

    @Test
    void getTrainingWithDayLinksShouldThrowExceptionTrainerNotFound() {
        Long userId = 61L;
        Mockito.when(
                this.userTrainerService.checkIfTrainerHasUser(this.testTrainer.getId(), userId))
                .thenReturn(false);
        Assertions.assertThrows(NoUserFoundForTrainerException.class,
                () -> this.trainerService.getTrainingWithDayLinks(this.testTrainer.getId(), userId, 1L), "Trainer  is not correct"
        );
    }

    @Test
    void getTrainingWithDayLinksShouldReturnNotNull() throws NoUserFoundForTrainerException {
        Long userId = 61L;
        TrainingWithLinksServiceModel serviceModel = new TrainingWithLinksServiceModel();
        Mockito.when(
                this.userTrainerService.checkIfTrainerHasUser(this.testTrainer.getId(), userId))
                .thenReturn(true);
        Mockito.when(
                this.trainingService.getTrainingWithDayLinks(userId, 1L))
                .thenReturn(serviceModel);
        TrainingWithLinksServiceModel model =
                this.trainerService.getTrainingWithDayLinks(this.testTrainer.getId(), userId, 1L);
        Assertions.assertEquals(serviceModel, model, "Trainer  is not correct");
    }

    /**
     * TrainerService - getFullTraining(...) TESTS
     */

    @Test
    void getFullTrainingShouldThrowExceptionTrainerNotFound() {
        Long userId = 61L;
        Mockito.when(
                this.userTrainerService.checkIfTrainerHasUser(this.testTrainer.getId(), userId))
                .thenReturn(false);
        Assertions.assertThrows(NoUserFoundForTrainerException.class,
                () -> this.trainerService.getFullTraining(this.testTrainer.getId(), userId, 1L), "Trainer  is not correct"
        );
    }

    @Test
    void getFullTrainingShouldReturnNotNull() throws NoUserFoundForTrainerException {
        Long userId = 61L;
        TrainingFullInfoServiceModel serviceModel = new TrainingFullInfoServiceModel();
        Mockito.when(
                this.userTrainerService.checkIfTrainerHasUser(this.testTrainer.getId(), userId))
                .thenReturn(true);
        Mockito.when(
                this.trainingService.getFullTraining(userId, 1L))
                .thenReturn(serviceModel);
        TrainingFullInfoServiceModel model =
                this.trainerService.getFullTraining(this.testTrainer.getId(), userId, 1L);
        Assertions.assertEquals(serviceModel, model, "Trainer  is not correct");
    }
}
