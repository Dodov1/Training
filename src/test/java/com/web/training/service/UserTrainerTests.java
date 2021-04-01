package com.web.training.service;


import com.web.training.models.exceptionModels.*;
import com.web.training.models.bindingModels.RatingBindingModel;
import com.web.training.models.bindingModels.RequestBindingModel;
import com.web.training.models.entities.*;
import com.web.training.models.enums.RelationStatus;
import com.web.training.models.enums.TrainerType;
import com.web.training.models.serviceModels.*;
import com.web.training.repositories.TrainerRepository;
import com.web.training.repositories.UserRepository;
import com.web.training.repositories.UserTrainerRepository;
import com.web.training.service.impl.UserTrainerServiceImpl;
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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@SpringBootTest
public class UserTrainerTests {

    @Mock
    private UserTrainerRepository userTrainerRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private TrainerRepository trainerRepository;

    private UserTrainerService userTrainerService;

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private final SortingValidator sortingValidator = new SortingValidatorImpl();
    private final ModelMapper modelMapper = new ModelMapper();

    private static final Long WRONG_TRAINER_ID = 55L;
    private static final Long WRONG_USER_ID = 55L;


    private User testUser;
    private User testUser2;
    private User testUser3;
    private Trainer testTrainer;
    private Trainer testTrainer2;
    private UserTrainer testUserTrainer;
    private UserTrainer testUserTrainer2;
    private UserTrainer testUserTrainer3;


    @BeforeEach
    public void init() {

        this.testTrainer = new Trainer() {{
            setType(TrainerType.Group);
            setId(1L);
            setStatus(RelationStatus.Accepted);
            setUser(testUser);
            setUsers(new HashSet<>());
        }};
        Picture p = new Picture();
        p.setLocation("DDSS");
        this.testUser = new User() {{
            setId(11L);
            setEnabled(true);
            setTrainer(testTrainer);
            setTrainers(new HashSet<>());
            setTrainings(new HashSet<>());
            setWeights(new HashSet<>());
            setFirstName("PeshoF");
            setLastName("PeshoL");
            setUsername("PeshoU");
            setEmail("Pesho@mail.bg");
            setAge(18);
            setPicture(p);
            setPassword(passwordEncoder.encode("asdfg"));
            setHeight(184);
        }};
        this.testTrainer.setUser(this.testUser);

        this.testTrainer2 = new Trainer() {{
            setType(TrainerType.Individual);
            setId(2L);
            setStatus(RelationStatus.Accepted);
            setUser(testUser);
            setUsers(new HashSet<>());
        }};
        Picture picture = new Picture();
        picture.setLocation("afds");
        this.testUser2 = new User() {{
            setId(25L);
            setEnabled(true);
            setTrainer(testTrainer2);
            setTrainers(new HashSet<>());
            setTrainings(new HashSet<>());
            setWeights(new HashSet<>());
            setPicture(picture);
            setFirstName("PeshoF222");
            setLastName("PeshoL222");
            setUsername("PeshoU222");
            setEmail("Pesho222@mail.bg");
            setAge(15);
            setPassword(passwordEncoder.encode("asdfg"));
            setHeight(164);
        }};

        this.testUser3 = new User() {{
            setId(1L);
            setEnabled(true);
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

        this.testUserTrainer = new UserTrainer() {{
            setId(1L);
            setUser(testUser);
            setTrainer(testTrainer);
            setIsTrainerRequester(true);
            setDate(LocalDate.now());
            setStatusType(RelationStatus.NotAnswered);
        }};

        this.testUserTrainer2 = new UserTrainer() {{
            setId(1L);
            setUser(testUser2);
            setTrainer(testTrainer2);
            setIsTrainerRequester(true);
            setDate(LocalDate.now());
            setStatusType(RelationStatus.Accepted);
        }};

        this.testUserTrainer3 = new UserTrainer() {{
            setId(1L);
            setUser(testUser3);
            setTrainer(testTrainer);
            setIsTrainerRequester(true);
            setDate(LocalDate.now());
            setStatusType(RelationStatus.Rejected);
        }};

        this.userTrainerService = new UserTrainerServiceImpl(this.userTrainerRepository, this.userRepository, this.trainerRepository, this.sortingValidator, this.modelMapper);
    }

    /**
     * UserTrainerService getAllRequestsForTrainer(...) Tests
     */

    @Test
    void getAllRequestsForTrainerShouldReturnCorrectSize() {
        Mockito.when(this.trainerRepository.findById(this.testTrainer.getId()))
                .thenReturn(Optional.of(this.testTrainer));
        Mockito.when(this.userTrainerRepository
                .getAllByTrainerIdAndStatusTypeAndIsTrainerRequesterIsFalse(this.testTrainer.getId(), RelationStatus.NotAnswered))
                .thenReturn(List.of(this.testUserTrainer2));
        List<RequestServiceModel> actual = this.userTrainerService.getAllRequestsForTrainer(this.testTrainer.getId());
        Assertions.assertEquals(1, actual.size(), "UserTrainer is not correct");
    }

    @Test
    void getAllRequestsForTrainerShouldReturnCorrectId() {
        Mockito.when(this.trainerRepository.findById(this.testTrainer.getId()))
                .thenReturn(Optional.of(this.testTrainer));
        Mockito.when(this.userTrainerRepository
                .getAllByTrainerIdAndStatusTypeAndIsTrainerRequesterIsFalse(this.testTrainer.getId(), RelationStatus.NotAnswered))
                .thenReturn(List.of(this.testUserTrainer2));
        List<RequestServiceModel> actual = this.userTrainerService.getAllRequestsForTrainer(this.testTrainer.getId());
        Assertions.assertEquals(this.testUserTrainer2.getUser().getId(), actual.get(0).getId(), "UserTrainer is not correct");
    }

    @Test
    void getAllRequestsForTrainerShouldReturnCorrectPicture() {
        Mockito.when(this.trainerRepository.findById(this.testTrainer.getId()))
                .thenReturn(Optional.of(this.testTrainer));
        Mockito.when(this.userTrainerRepository
                .getAllByTrainerIdAndStatusTypeAndIsTrainerRequesterIsFalse(this.testTrainer.getId(), RelationStatus.NotAnswered))
                .thenReturn(List.of(this.testUserTrainer2));
        List<RequestServiceModel> actual = this.userTrainerService.getAllRequestsForTrainer(this.testTrainer.getId());
        Assertions.assertEquals(this.testUserTrainer2.getUser().getPicture().getLocation(), actual.get(0).getProfilePicture(), "UserTrainer is not correct");
    }

    @Test
    void getAllRequestsForTrainerShouldReturnCorrectZeroSize() {
        Mockito.when(this.trainerRepository.findById(this.testTrainer.getId()))
                .thenReturn(Optional.of(this.testTrainer));
        Mockito.when(this.userTrainerRepository
                .getAllByTrainerIdAndStatusTypeAndIsTrainerRequesterIsFalse(this.testTrainer.getId(), RelationStatus.NotAnswered))
                .thenReturn(List.of());
        List<RequestServiceModel> actual = this.userTrainerService.getAllRequestsForTrainer(this.testTrainer.getId());
        Assertions.assertEquals(0, actual.size(), "UserTrainer is not correct");
    }

    @Test
    void getAllRequestsForTrainerShouldThrowNoTrainerException() {
        Mockito.when(this.trainerRepository.findById(this.testTrainer.getId()))
                .thenReturn(Optional.of(this.testTrainer));
        Assertions.assertThrows(TrainerNotFoundException.class, () ->
                this.userTrainerService.getAllRequestsForTrainer(WRONG_TRAINER_ID), "UserTrainer is not correct");
    }

    /**
     * UserTrainerService getAllRequestsForUser(...) Tests
     */

    @Test
    void getAllRequestsForUserShouldReturnCorrectSize() {
        Mockito.when(this.userRepository.findById(this.testUser.getId()))
                .thenReturn(Optional.of(this.testUser));
        Mockito.when(this.userTrainerRepository
                .getAllByUserIdAndStatusTypeAndIsTrainerRequesterIsTrue(this.testUser.getId(), RelationStatus.NotAnswered))
                .thenReturn(List.of(this.testUserTrainer2));
        List<RequestServiceModel> actual = this.userTrainerService.getAllRequestsForUser(this.testUser.getId());
        Assertions.assertEquals(1, actual.size(), "UserTrainer is not correct");
    }

    @Test
    void getAllRequestsForUserShouldReturnCorrectId() {
        Mockito.when(this.userRepository.findById(this.testUser.getId()))
                .thenReturn(Optional.of(this.testUser));
        Mockito.when(this.userTrainerRepository
                .getAllByUserIdAndStatusTypeAndIsTrainerRequesterIsTrue(this.testUser.getId(), RelationStatus.NotAnswered))
                .thenReturn(List.of(this.testUserTrainer2));
        List<RequestServiceModel> actual = this.userTrainerService.getAllRequestsForUser(this.testUser.getId());
        Assertions.assertEquals(this.testUserTrainer2.getTrainer().getId(), actual.get(0).getId(), "UserTrainer is not correct");
    }

    @Test
    void getAllRequestsForUserShouldReturnCorrectPicture() {
        Mockito.when(this.userRepository.findById(this.testUser.getId()))
                .thenReturn(Optional.of(this.testUser));
        Mockito.when(this.userTrainerRepository
                .getAllByUserIdAndStatusTypeAndIsTrainerRequesterIsTrue(this.testUser.getId(), RelationStatus.NotAnswered))
                .thenReturn(List.of(this.testUserTrainer2));
        List<RequestServiceModel> actual = this.userTrainerService.getAllRequestsForUser(this.testUser.getId());
        Assertions.assertEquals(this.testUserTrainer2.getTrainer().getUser().getPicture().getLocation(), actual.get(0).getProfilePicture(), "UserTrainer is not correct");
    }

    @Test
    void getAllRequestsForUserShouldReturnCorrectZeroSize() {
        Mockito.when(this.userRepository.findById(this.testUser.getId()))
                .thenReturn(Optional.of(this.testUser));
        Mockito.when(this.userTrainerRepository
                .getAllByUserIdAndStatusTypeAndIsTrainerRequesterIsTrue(this.testTrainer.getId(), RelationStatus.NotAnswered))
                .thenReturn(List.of());
        List<RequestServiceModel> actual = this.userTrainerService.getAllRequestsForUser(this.testUser.getId());
        Assertions.assertEquals(0, actual.size(), "UserTrainer is not correct");
    }

    @Test
    void getAllRequestsForUserShouldThrowNoTrainerException() {
        Mockito.when(this.userRepository.findById(this.testUser.getId()))
                .thenReturn(Optional.of(this.testUser));
        Assertions.assertThrows(UserNotFoundException.class, () ->
                this.userTrainerService.getAllRequestsForUser(WRONG_USER_ID), "UserTrainer is not correct");
    }

    /**
     * UserTrainerService requestManager(...) Tests
     */

    @Test
    void requestManagerShouldChangeEntityStatus() {
        this.testUserTrainer.setStatusType(RelationStatus.NotAnswered);
        Mockito.when(this.trainerRepository.findById(this.testTrainer.getId()))
                .thenReturn(Optional.of(this.testTrainer));
        Mockito.when(this.userRepository.findById(this.testUser.getId()))
                .thenReturn(Optional.of(this.testUser));
        Mockito.when(this.userTrainerRepository
                .getByTrainerIdAndUserId(this.testTrainer.getId(), this.testUser.getId()))
                .thenReturn(Optional.of(this.testUserTrainer));
        RequestBindingModel requestBindingModel = new RequestBindingModel();
        requestBindingModel.setStatusType(RelationStatus.Rejected);
        this.userTrainerService.requestManager(requestBindingModel, this.testTrainer.getId(), this.testUser.getId(), true);
        Assertions.assertEquals(RelationStatus.Rejected, this.testUserTrainer.getStatusType(), "UserTrainer is not correct");
    }

    @Test
    void requestManagerShouldCreateEntity() {
        this.testUserTrainer.setStatusType(RelationStatus.NotAnswered);
        Mockito.when(this.trainerRepository.findById(this.testTrainer2.getId()))
                .thenReturn(Optional.of(this.testTrainer2));
        Mockito.when(this.userRepository.findById(this.testUser.getId()))
                .thenReturn(Optional.of(this.testUser));
        Mockito.when(this.userTrainerRepository
                .getByTrainerIdAndUserId(this.testTrainer.getId(), this.testUser.getId()))
                .thenReturn(Optional.of(this.testUserTrainer));
        RequestBindingModel requestBindingModel = new RequestBindingModel();
        requestBindingModel.setStatusType(RelationStatus.Rejected);
        this.userTrainerService.requestManager(requestBindingModel, this.testTrainer2.getId(), this.testUser.getId(), true);
        Assertions.assertEquals(RelationStatus.NotAnswered, this.testUserTrainer.getStatusType(), "UserTrainer is not correct");
    }

    @Test
    void requestManagerShouldPersistTrainerEntity() {
        Mockito.when(this.trainerRepository.findById(this.testTrainer.getId()))
                .thenReturn(Optional.of(this.testTrainer));
        Mockito.when(this.userRepository.findById(this.testUser.getId()))
                .thenReturn(Optional.of(this.testUser));
        Mockito.when(this.userTrainerRepository
                .getByTrainerIdAndUserId(this.testTrainer.getId(), this.testUser.getId()))
                .thenReturn(Optional.of(this.testUserTrainer));
        RequestBindingModel requestBindingModel = new RequestBindingModel();
        this.userTrainerService.requestManager(requestBindingModel, this.testTrainer.getId(), this.testUser.getId(), true);
        Assertions.assertEquals(this.testTrainer, this.testUserTrainer.getTrainer(), "UserTrainer is not correct");
    }

    @Test
    void requestManagerShouldPersistUserEntity() {
        Mockito.when(this.trainerRepository.findById(this.testTrainer.getId()))
                .thenReturn(Optional.of(this.testTrainer));
        Mockito.when(this.userRepository.findById(this.testUser.getId()))
                .thenReturn(Optional.of(this.testUser));
        Mockito.when(this.userTrainerRepository
                .getByTrainerIdAndUserId(this.testTrainer.getId(), this.testUser.getId()))
                .thenReturn(Optional.of(this.testUserTrainer));
        RequestBindingModel requestBindingModel = new RequestBindingModel();
        this.userTrainerService.requestManager(requestBindingModel, this.testTrainer.getId(), this.testUser.getId(), true);
        Assertions.assertEquals(this.testUser, this.testUserTrainer.getUser(), "UserTrainer is not correct");
    }

    @Test
    void requestManagerShouldNotIsTrainerRequesterTrue() {
        Mockito.when(this.trainerRepository.findById(this.testTrainer.getId()))
                .thenReturn(Optional.of(this.testTrainer));
        Mockito.when(this.userRepository.findById(this.testUser.getId()))
                .thenReturn(Optional.of(this.testUser));
        Mockito.when(this.userTrainerRepository
                .getByTrainerIdAndUserId(this.testTrainer.getId(), this.testUser.getId()))
                .thenReturn(Optional.of(this.testUserTrainer));
        RequestBindingModel requestBindingModel = new RequestBindingModel();
        this.userTrainerService.requestManager(requestBindingModel, this.testTrainer.getId(), this.testUser.getId(), true);
        Assertions.assertTrue(this.testUserTrainer.getIsTrainerRequester(), "UserTrainer is not correct");
    }

    @Test
    void requestManagerShouldNotIsTrainerRequesterFalse() {
        Mockito.when(this.trainerRepository.findById(this.testTrainer.getId()))
                .thenReturn(Optional.of(this.testTrainer));
        Mockito.when(this.userRepository.findById(this.testUser.getId()))
                .thenReturn(Optional.of(this.testUser));
        Mockito.when(this.userTrainerRepository
                .getByTrainerIdAndUserId(this.testTrainer.getId(), this.testUser.getId()))
                .thenReturn(Optional.of(this.testUserTrainer));
        RequestBindingModel requestBindingModel = new RequestBindingModel();
        this.userTrainerService.requestManager(requestBindingModel, this.testTrainer.getId(), this.testUser.getId(), false);
        Assertions.assertFalse(this.testUserTrainer.getIsTrainerRequester(), "UserTrainer is not correct");
    }

    @Test
    void requestManagerShouldThrowExceptionNoTrainer() {
        Mockito.when(this.trainerRepository.findById(this.testTrainer.getId()))
                .thenReturn(Optional.of(this.testTrainer));
        Mockito.when(this.userRepository.findById(this.testUser.getId()))
                .thenReturn(Optional.of(this.testUser));
        Mockito.when(this.userTrainerRepository
                .getByTrainerIdAndUserId(this.testTrainer.getId(), this.testUser.getId()))
                .thenReturn(Optional.of(this.testUserTrainer));
        RequestBindingModel requestBindingModel = new RequestBindingModel();
        Assertions.assertThrows(TrainerNotFoundException.class,
                () -> this.userTrainerService.requestManager(requestBindingModel, this.testTrainer2.getId(), this.testUser.getId(), false),
                "UserTrainer is not correct");
    }

    @Test
    void requestManagerShouldThrowExceptionNoUser() {
        Mockito.when(this.trainerRepository.findById(this.testTrainer.getId()))
                .thenReturn(Optional.of(this.testTrainer));
        Mockito.when(this.userRepository.findById(this.testUser.getId()))
                .thenReturn(Optional.of(this.testUser));
        Mockito.when(this.userTrainerRepository
                .getByTrainerIdAndUserId(this.testTrainer.getId(), this.testUser.getId()))
                .thenReturn(Optional.of(this.testUserTrainer));
        RequestBindingModel requestBindingModel = new RequestBindingModel();
        Assertions.assertThrows(UserNotFoundException.class,
                () -> this.userTrainerService.requestManager(requestBindingModel, this.testTrainer.getId(), this.testUser2.getId(), false),
                "UserTrainer is not correct");
    }

    /**
     * UserTrainerService getUserByIdForTrainer(...) Tests
     */

    @Test
    void getUserByIdForTrainerShouldReturnCorrectUserId() {
        Mockito.when(this.userTrainerRepository
                .getByTrainerIdAndUserId(this.testTrainer.getId(), this.testUser.getId()))
                .thenReturn(Optional.of(this.testUserTrainer));
        UserProfileServiceModel actual = this.userTrainerService.getUserByIdForTrainer(this.testTrainer.getId(), this.testUser.getId());
        Assertions.assertEquals(this.testUser.getId(), actual.getId(), "UserTrainer is not correct");
    }

    @Test
    void getUserByIdForTrainerShouldReturnCorrectUserAge() {
        Mockito.when(this.userTrainerRepository
                .getByTrainerIdAndUserId(this.testTrainer.getId(), this.testUser.getId()))
                .thenReturn(Optional.of(this.testUserTrainer));
        UserProfileServiceModel actual = this.userTrainerService.getUserByIdForTrainer(this.testTrainer.getId(), this.testUser.getId());
        Assertions.assertEquals(this.testUser.getAge(), actual.getAge(), "UserTrainer is not correct");
    }

    @Test
    void getUserByIdForTrainerShouldReturnCorrectUserHeight() {
        Mockito.when(this.userTrainerRepository
                .getByTrainerIdAndUserId(this.testTrainer.getId(), this.testUser.getId()))
                .thenReturn(Optional.of(this.testUserTrainer));
        UserProfileServiceModel actual = this.userTrainerService.getUserByIdForTrainer(this.testTrainer.getId(), this.testUser.getId());
        Assertions.assertEquals(this.testUser.getHeight(), actual.getHeight(), "UserTrainer is not correct");
    }

    @Test
    void getUserByIdForTrainerShouldReturnCorrectUserFirstName() {
        Mockito.when(this.userTrainerRepository
                .getByTrainerIdAndUserId(this.testTrainer.getId(), this.testUser.getId()))
                .thenReturn(Optional.of(this.testUserTrainer));
        UserProfileServiceModel actual = this.userTrainerService.getUserByIdForTrainer(this.testTrainer.getId(), this.testUser.getId());
        Assertions.assertEquals(this.testUser.getFirstName(), actual.getFirstName(), "UserTrainer is not correct");
    }

    @Test
    void getUserByIdForTrainerShouldReturnCorrectUserLastName() {
        Mockito.when(this.userTrainerRepository
                .getByTrainerIdAndUserId(this.testTrainer.getId(), this.testUser.getId()))
                .thenReturn(Optional.of(this.testUserTrainer));
        UserProfileServiceModel actual = this.userTrainerService.getUserByIdForTrainer(this.testTrainer.getId(), this.testUser.getId());
        Assertions.assertEquals(this.testUser.getLastName(), actual.getLastName(), "UserTrainer is not correct");
    }

    @Test
    void getUserByIdForTrainerShouldReturnCorrectUserEmail() {
        Mockito.when(this.userTrainerRepository
                .getByTrainerIdAndUserId(this.testTrainer.getId(), this.testUser.getId()))
                .thenReturn(Optional.of(this.testUserTrainer));
        UserProfileServiceModel actual = this.userTrainerService.getUserByIdForTrainer(this.testTrainer.getId(), this.testUser.getId());
        Assertions.assertEquals(this.testUser.getEmail(), actual.getEmail(), "UserTrainer is not correct");
    }

    @Test
    void getUserByIdForTrainerShouldReturnCorrectUserStatus() {
        Mockito.when(this.userTrainerRepository
                .getByTrainerIdAndUserId(this.testTrainer.getId(), this.testUser.getId()))
                .thenReturn(Optional.of(this.testUserTrainer));
        UserProfileServiceModel actual = this.userTrainerService.getUserByIdForTrainer(this.testTrainer.getId(), this.testUser.getId());
        Assertions.assertEquals(this.testUserTrainer.getStatusType(), actual.getStatusType(), "UserTrainer is not correct");
    }

    @Test
    void getUserByIdForTrainerShouldThrowExceptionNoUser() {
        Mockito.when(this.userTrainerRepository
                .getByTrainerIdAndUserId(this.testTrainer.getId(), this.testUser.getId()))
                .thenReturn(Optional.of(this.testUserTrainer));
        Assertions.assertThrows(UserNotFoundException.class,
                () -> this.userTrainerService.getUserByIdForTrainer(this.testTrainer.getId(), this.testUser2.getId()),
                "UserTrainer is not correct");
    }

    /**
     * UserTrainerService getTrainerByIdForUser(...) Tests
     */

    @Test
    void getTrainerByIdForUserShouldReturnCorrectUserId() {
        Mockito.when(this.userTrainerRepository
                .getByTrainerIdAndUserId(this.testTrainer.getId(), this.testUser.getId()))
                .thenReturn(Optional.of(this.testUserTrainer));
        UserProfileServiceModel actual = this.userTrainerService.getTrainerByIdForUser(this.testUser.getId(), this.testTrainer.getId());
        Assertions.assertEquals(this.testUser.getTrainer().getId(), actual.getId(), "UserTrainer is not correct");
    }

    @Test
    void getTrainerByIdForUserShouldReturnCorrectUserAge() {
        this.testTrainer.setUser(this.testUser2);
        Mockito.when(this.userTrainerRepository
                .getByTrainerIdAndUserId(this.testTrainer.getId(), this.testUser.getId()))
                .thenReturn(Optional.of(this.testUserTrainer));
        UserProfileServiceModel actual = this.userTrainerService.getTrainerByIdForUser(this.testUser.getId(), this.testTrainer.getId());
        Assertions.assertEquals(this.testUser.getTrainer().getUser().getAge(), actual.getAge(), "UserTrainer is not correct");
    }

    @Test
    void getTrainerByIdForUserShouldReturnCorrectUserHeight() {
        this.testTrainer.setUser(this.testUser2);
        Mockito.when(this.userTrainerRepository
                .getByTrainerIdAndUserId(this.testTrainer.getId(), this.testUser.getId()))
                .thenReturn(Optional.of(this.testUserTrainer));
        UserProfileServiceModel actual = this.userTrainerService.getTrainerByIdForUser(this.testUser.getId(), this.testTrainer.getId());
        Assertions.assertEquals(this.testUser.getTrainer().getUser().getHeight(), actual.getHeight(), "UserTrainer is not correct");
    }

    @Test
    void getTrainerByIdForUserShouldReturnCorrectUserFirstName() {
        this.testTrainer.setUser(this.testUser2);
        Mockito.when(this.userTrainerRepository
                .getByTrainerIdAndUserId(this.testTrainer.getId(), this.testUser.getId()))
                .thenReturn(Optional.of(this.testUserTrainer));
        UserProfileServiceModel actual = this.userTrainerService.getTrainerByIdForUser(this.testUser.getId(), this.testTrainer.getId());
        Assertions.assertEquals(this.testUser.getTrainer().getUser().getFirstName(), actual.getFirstName(), "UserTrainer is not correct");
    }

    @Test
    void getTrainerByIdForUserShouldReturnCorrectUserLastName() {
        this.testTrainer.setUser(this.testUser2);
        Mockito.when(this.userTrainerRepository
                .getByTrainerIdAndUserId(this.testTrainer.getId(), this.testUser.getId()))
                .thenReturn(Optional.of(this.testUserTrainer));
        UserProfileServiceModel actual = this.userTrainerService.getTrainerByIdForUser(this.testUser.getId(), this.testTrainer.getId());
        Assertions.assertEquals(this.testUser.getTrainer().getUser().getLastName(), actual.getLastName(), "UserTrainer is not correct");
    }

    @Test
    void getTrainerByIdForUserShouldReturnCorrectUserEmail() {
        this.testTrainer.setUser(this.testUser2);
        Mockito.when(this.userTrainerRepository
                .getByTrainerIdAndUserId(this.testTrainer.getId(), this.testUser.getId()))
                .thenReturn(Optional.of(this.testUserTrainer));
        UserProfileServiceModel actual = this.userTrainerService.getTrainerByIdForUser(this.testUser.getId(), this.testTrainer.getId());
        Assertions.assertEquals(this.testUser.getTrainer().getUser().getEmail(), actual.getEmail(), "UserTrainer is not correct");
    }

    @Test
    void getTrainerByIdForUserShouldReturnCorrectUserStatus() {
        Mockito.when(this.userTrainerRepository
                .getByTrainerIdAndUserId(this.testTrainer.getId(), this.testUser.getId()))
                .thenReturn(Optional.of(this.testUserTrainer));
        UserProfileServiceModel actual = this.userTrainerService.getTrainerByIdForUser(this.testUser.getId(), this.testTrainer.getId());
        Assertions.assertEquals(this.testUserTrainer.getStatusType(), actual.getStatusType(), "UserTrainer is not correct");
    }

    @Test
    void getTrainerByIdForUserShouldThrowExceptionNoUser() {
        Mockito.when(this.userTrainerRepository
                .getByTrainerIdAndUserId(this.testTrainer.getId(), this.testUser.getId()))
                .thenReturn(Optional.of(this.testUserTrainer));
        Assertions.assertThrows(TrainerNotFoundException.class,
                () -> this.userTrainerService.getTrainerByIdForUser(this.testUser.getId(), this.testTrainer2.getId()),
                "UserTrainer is not correct");
    }

    /**
     * UserTrainerService getUsersPageForTrainerId(...) Tests
     */

    @Test
    void getUsersPageForTrainerIdShouldReturnCorrectOneSize() {
        Mockito.when(this.trainerRepository.findById(this.testTrainer.getId()))
                .thenReturn(Optional.of(this.testTrainer));
        Mockito.when(this.userTrainerRepository
                .getByTrainerIdAndUserId(this.testTrainer.getId(), this.testUser.getId()))
                .thenReturn(Optional.of(this.testUserTrainer));
        Mockito.when(this.userTrainerRepository
                .getAllByTrainerIdAndStatusType(Mockito.eq(this.testTrainer.getId()), Mockito.eq(RelationStatus.Accepted), Mockito.any(Pageable.class)))
                .thenReturn(List.of(this.testUserTrainer));
        List<UserTrainerInfoServiceModel> actual = this.userTrainerService.getUsersPageForTrainerId(this.testTrainer.getId(), 0, 1, "age", "asc");
        Assertions.assertEquals(1, actual.size(), "UserTrainer is not correct");
    }

    @Test
    void getUsersPageForTrainerIdShouldReturnCorrectTwoSize() {
        Mockito.when(this.trainerRepository.findById(this.testTrainer.getId()))
                .thenReturn(Optional.of(this.testTrainer));
        Mockito.when(this.userTrainerRepository
                .getByTrainerIdAndUserId(this.testTrainer.getId(), this.testUser.getId()))
                .thenReturn(Optional.of(this.testUserTrainer));
        Mockito.when(this.userTrainerRepository
                .getAllByTrainerIdAndStatusType(Mockito.eq(this.testTrainer.getId()), Mockito.eq(RelationStatus.Accepted), Mockito.any(Pageable.class)))
                .thenReturn(List.of(this.testUserTrainer, this.testUserTrainer2));
        List<UserTrainerInfoServiceModel> actual = this.userTrainerService.getUsersPageForTrainerId(this.testTrainer.getId(), 0, 2, "age", "asc");
        Assertions.assertEquals(2, actual.size(), "UserTrainer is not correct");
    }

    @Test
    void getUsersPageForTrainerIdShouldReturnCorrectCustomSortingTrainingAsc() {
        Mockito.when(this.trainerRepository.findById(this.testTrainer.getId()))
                .thenReturn(Optional.of(this.testTrainer));
        Mockito.when(this.userTrainerRepository
                .getByTrainerIdAndUserId(this.testTrainer.getId(), this.testUser.getId()))
                .thenReturn(Optional.of(this.testUserTrainer));
        Mockito.when(this.userTrainerRepository
                .getAllUsersTrainerIdSortedByTotalTrainingsAsc(this.testTrainer.getId(), 0L, 1L))
                .thenReturn(List.of(this.testUserTrainer));
        List<UserTrainerInfoServiceModel> actual = this.userTrainerService.getUsersPageForTrainerId(this.testTrainer.getId(), 0, 1, "totalTrainings", "asc");
        Assertions.assertEquals(1, actual.size(), "UserTrainer is not correct");
    }

    @Test
    void getUsersPageForTrainerIdShouldReturnCorrectCustomSortingTrainingDesc() {
        Mockito.when(this.trainerRepository.findById(this.testTrainer.getId()))
                .thenReturn(Optional.of(this.testTrainer));
        Mockito.when(this.userTrainerRepository
                .getByTrainerIdAndUserId(this.testTrainer.getId(), this.testUser.getId()))
                .thenReturn(Optional.of(this.testUserTrainer));
        Mockito.when(this.userTrainerRepository
                .getAllUsersTrainerIdSortedByTotalTrainingsDesc(this.testTrainer.getId(), 0L, 1L))
                .thenReturn(List.of(this.testUserTrainer));
        List<UserTrainerInfoServiceModel> actual = this.userTrainerService.getUsersPageForTrainerId(this.testTrainer.getId(), 0, 1, "totalTrainings", "desc");
        Assertions.assertEquals(1, actual.size(), "UserTrainer is not correct");
    }

    @Test
    void getUsersPageForTrainerIdShouldReturnCorrectCustomSortingTrainingAsc2() {
        Mockito.when(this.trainerRepository.findById(this.testTrainer.getId()))
                .thenReturn(Optional.of(this.testTrainer));
        Mockito.when(this.userTrainerRepository
                .getByTrainerIdAndUserId(this.testTrainer.getId(), this.testUser.getId()))
                .thenReturn(Optional.of(this.testUserTrainer));
        Mockito.when(this.userTrainerRepository
                .getAllUsersTrainerIdSortedByTotalTrainingsAsc(this.testTrainer.getId(), 2L, 4L))
                .thenReturn(List.of(this.testUserTrainer, this.testUserTrainer2));
        List<UserTrainerInfoServiceModel> actual = this.userTrainerService.getUsersPageForTrainerId(this.testTrainer.getId(), 1, 2, "totalTrainings", "asc");
        Assertions.assertEquals(2, actual.size(), "UserTrainer is not correct");
    }

    @Test
    void getUsersPageForTrainerIdShouldReturnCorrectCustomSortingTrainingDesc2() {
        Mockito.when(this.trainerRepository.findById(this.testTrainer.getId()))
                .thenReturn(Optional.of(this.testTrainer));
        Mockito.when(this.userTrainerRepository
                .getByTrainerIdAndUserId(this.testTrainer.getId(), this.testUser.getId()))
                .thenReturn(Optional.of(this.testUserTrainer));
        Mockito.when(this.userTrainerRepository
                .getAllUsersTrainerIdSortedByTotalTrainingsDesc(this.testTrainer.getId(), 2L, 4L))
                .thenReturn(List.of(this.testUserTrainer, this.testUserTrainer2));
        List<UserTrainerInfoServiceModel> actual = this.userTrainerService.getUsersPageForTrainerId(this.testTrainer.getId(), 1, 2, "totalTrainings", "desc");
        Assertions.assertEquals(2, actual.size(), "UserTrainer is not correct");
    }

    @Test
    void getUsersPageForTrainerIdShouldReturnCorrectSizeTrainings() {
        this.testUser.getTrainings().add(new Training());
        this.testUser.getTrainings().add(new Training());
        this.testUser.getTrainings().add(new Training());
        this.testUser.getTrainings().add(new Training());
        Mockito.when(this.trainerRepository.findById(this.testTrainer.getId()))
                .thenReturn(Optional.of(this.testTrainer));
        Mockito.when(this.userTrainerRepository
                .getByTrainerIdAndUserId(this.testTrainer.getId(), this.testUser.getId()))
                .thenReturn(Optional.of(this.testUserTrainer));
        Mockito.when(this.userTrainerRepository
                .getAllByTrainerIdAndStatusType(Mockito.eq(this.testTrainer.getId()), Mockito.eq(RelationStatus.Accepted), Mockito.any(Pageable.class)))
                .thenReturn(List.of(this.testUserTrainer));
        Mockito.when(this.userRepository.countAllTrainingsById(this.testUser.getId()))
                .thenReturn(Optional.of(this.testUser.getTrainings().size()));
        List<UserTrainerInfoServiceModel> actual = this.userTrainerService.getUsersPageForTrainerId(this.testTrainer.getId(), 1, 2, "age", "desc");
        Assertions.assertEquals(this.testUser.getTrainings().size(), actual.get(0).getTrainingCount(), "UserTrainer is not correct");
    }

    @Test
    void getUsersPageForTrainerIdShouldReturnCorrectZeroSizeTrainings() {
        Mockito.when(this.trainerRepository.findById(this.testTrainer.getId()))
                .thenReturn(Optional.of(this.testTrainer));
        Mockito.when(this.userTrainerRepository
                .getByTrainerIdAndUserId(this.testTrainer.getId(), this.testUser.getId()))
                .thenReturn(Optional.of(this.testUserTrainer));
        Mockito.when(this.userTrainerRepository
                .getAllByTrainerIdAndStatusType(Mockito.eq(this.testTrainer.getId()), Mockito.eq(RelationStatus.Accepted), Mockito.any(Pageable.class)))
                .thenReturn(List.of(this.testUserTrainer));
        Mockito.when(this.userRepository.countAllTrainingsById(this.testUser.getId()))
                .thenReturn(Optional.empty());
        List<UserTrainerInfoServiceModel> actual = this.userTrainerService.getUsersPageForTrainerId(this.testTrainer.getId(), 1, 2, "age", "desc");
        Assertions.assertEquals(this.testUser.getTrainings().size(), actual.get(0).getTrainingCount(), "UserTrainer is not correct");
    }

    @Test
    void getUsersPageForTrainerIdShouldReturnCorrectAge() {
        this.testUser.getTrainings().add(new Training());
        this.testUser.getTrainings().add(new Training());
        this.testUser.getTrainings().add(new Training());
        this.testUser.getTrainings().add(new Training());
        Mockito.when(this.trainerRepository.findById(this.testTrainer.getId()))
                .thenReturn(Optional.of(this.testTrainer));
        Mockito.when(this.userTrainerRepository
                .getByTrainerIdAndUserId(this.testTrainer.getId(), this.testUser.getId()))
                .thenReturn(Optional.of(this.testUserTrainer));
        Mockito.when(this.userTrainerRepository
                .getAllByTrainerIdAndStatusType(Mockito.eq(this.testTrainer.getId()), Mockito.eq(RelationStatus.Accepted), Mockito.any(Pageable.class)))
                .thenReturn(List.of(this.testUserTrainer));
        List<UserTrainerInfoServiceModel> actual = this.userTrainerService.getUsersPageForTrainerId(this.testTrainer.getId(), 1, 2, "age", "desc");
        Assertions.assertEquals(this.testUser.getAge(), actual.get(0).getAge(), "UserTrainer is not correct");
    }

    @Test
    void getUsersPageForTrainerIdShouldReturnCorrectHeight() {
        this.testUser.getTrainings().add(new Training());
        this.testUser.getTrainings().add(new Training());
        this.testUser.getTrainings().add(new Training());
        this.testUser.getTrainings().add(new Training());
        Mockito.when(this.trainerRepository.findById(this.testTrainer.getId()))
                .thenReturn(Optional.of(this.testTrainer));
        Mockito.when(this.userTrainerRepository
                .getByTrainerIdAndUserId(this.testTrainer.getId(), this.testUser.getId()))
                .thenReturn(Optional.of(this.testUserTrainer));
        Mockito.when(this.userTrainerRepository
                .getAllByTrainerIdAndStatusType(Mockito.eq(this.testTrainer.getId()), Mockito.eq(RelationStatus.Accepted), Mockito.any(Pageable.class)))
                .thenReturn(List.of(this.testUserTrainer));
        List<UserTrainerInfoServiceModel> actual = this.userTrainerService.getUsersPageForTrainerId(this.testTrainer.getId(), 1, 2, "age", "desc");
        Assertions.assertEquals(this.testUser.getHeight(), actual.get(0).getHeight(), "UserTrainer is not correct");
    }

    @Test
    void getUsersPageForTrainerIdShouldReturnCorrectUsername() {
        this.testUser.getTrainings().add(new Training());
        this.testUser.getTrainings().add(new Training());
        this.testUser.getTrainings().add(new Training());
        this.testUser.getTrainings().add(new Training());
        Mockito.when(this.trainerRepository.findById(this.testTrainer.getId()))
                .thenReturn(Optional.of(this.testTrainer));
        Mockito.when(this.userTrainerRepository
                .getByTrainerIdAndUserId(this.testTrainer.getId(), this.testUser.getId()))
                .thenReturn(Optional.of(this.testUserTrainer));
        Mockito.when(this.userTrainerRepository
                .getAllByTrainerIdAndStatusType(Mockito.eq(this.testTrainer.getId()), Mockito.eq(RelationStatus.Accepted), Mockito.any(Pageable.class)))
                .thenReturn(List.of(this.testUserTrainer));
        List<UserTrainerInfoServiceModel> actual = this.userTrainerService.getUsersPageForTrainerId(this.testTrainer.getId(), 1, 2, "age", "desc");
        Assertions.assertEquals(this.testUser.getUsername(), actual.get(0).getUsername(), "UserTrainer is not correct");
    }

    @Test
    void getUsersPageForTrainerIdShouldReturnCorrectId() {
        this.testUser.getTrainings().add(new Training());
        this.testUser.getTrainings().add(new Training());
        this.testUser.getTrainings().add(new Training());
        this.testUser.getTrainings().add(new Training());
        Mockito.when(this.trainerRepository.findById(this.testTrainer.getId()))
                .thenReturn(Optional.of(this.testTrainer));
        Mockito.when(this.userTrainerRepository
                .getByTrainerIdAndUserId(this.testTrainer.getId(), this.testUser.getId()))
                .thenReturn(Optional.of(this.testUserTrainer));
        Mockito.when(this.userTrainerRepository
                .getAllByTrainerIdAndStatusType(Mockito.eq(this.testTrainer.getId()), Mockito.eq(RelationStatus.Accepted), Mockito.any(Pageable.class)))
                .thenReturn(List.of(this.testUserTrainer));
        List<UserTrainerInfoServiceModel> actual = this.userTrainerService.getUsersPageForTrainerId(this.testTrainer.getId(), 1, 2, "age", "desc");
        Assertions.assertEquals(this.testUser.getId(), actual.get(0).getId(), "UserTrainer is not correct");
    }

    @Test
    void getUsersPageForWrongTrainerIdShouldThrowException() {
        Mockito.when(this.trainerRepository.findById(this.testTrainer.getId()))
                .thenReturn(Optional.of(this.testTrainer));
        Assertions.assertThrows(NotFoundException.class,
                () -> this.userTrainerService.getUsersPageForTrainerId(this.testTrainer2.getId(), 1, 2, "age", "desc"),
                "UserTrainer is not correct");
    }

    @Test
    void getUsersPageForWrongSortingTypeShouldThrowException() {
        Mockito.when(this.trainerRepository.findById(this.testTrainer.getId()))
                .thenReturn(Optional.of(this.testTrainer));
        Assertions.assertThrows(InvalidSortingTypeException.class,
                () -> this.userTrainerService.getUsersPageForTrainerId(this.testTrainer.getId(), 1, 2, "agde", "desc"),
                "UserTrainer is not correct");
    }

    @Test
    void getUsersPageForWrongSortingTypeShouldThrowException2() {
        Mockito.when(this.trainerRepository.findById(this.testTrainer.getId()))
                .thenReturn(Optional.of(this.testTrainer));
        Assertions.assertThrows(InvalidSortingTypeException.class,
                () -> this.userTrainerService.getUsersPageForTrainerId(this.testTrainer.getId(), 1, 2, "age", "defdssc"),
                "UserTrainer is not correct");
    }

    @Test
    void getUsersPageForWrongSortingTypeShouldThrowException3() {
        Mockito.when(this.trainerRepository.findById(this.testTrainer.getId()))
                .thenReturn(Optional.of(this.testTrainer));
        Assertions.assertThrows(InvalidSortingTypeException.class,
                () -> this.userTrainerService.getUsersPageForTrainerId(this.testTrainer.getId(), 1, 2, "agde", "defdssc"),
                "UserTrainer is not correct");
    }

    /**
     * UserTrainerService getTrainersPageForUserId(...) Tests
     */

    @Test
    void getTrainersPageForUserIdShouldReturnCorrectOneSize() {
        Mockito.when(this.userRepository.findById(this.testUser.getId()))
                .thenReturn(Optional.of(this.testUser));
        Mockito.when(this.userTrainerRepository
                .getByTrainerIdAndUserId(this.testTrainer.getId(), this.testUser.getId()))
                .thenReturn(Optional.of(this.testUserTrainer));
        Mockito.when(this.userTrainerRepository
                .getAllByUserIdAndStatusType(Mockito.eq(this.testUser.getId()), Mockito.eq(RelationStatus.Accepted), Mockito.any(Pageable.class)))
                .thenReturn(List.of(this.testUserTrainer));
        List<TrainerUserInfoServiceModel> actual = this.userTrainerService.getTrainersPageForUserId(this.testUser.getId(), 0, 1, "rating", "asc");
        Assertions.assertEquals(1, actual.size(), "UserTrainer is not correct");
    }

    @Test
    void getTrainersPageForUserIdShouldReturnCorrectTwoSize() {
        Mockito.when(this.userRepository.findById(this.testUser.getId()))
                .thenReturn(Optional.of(this.testUser));
        Mockito.when(this.userTrainerRepository
                .getByTrainerIdAndUserId(this.testTrainer.getId(), this.testUser.getId()))
                .thenReturn(Optional.of(this.testUserTrainer));
        Mockito.when(this.userTrainerRepository
                .getAllByUserIdAndStatusType(Mockito.eq(this.testUser.getId()), Mockito.eq(RelationStatus.Accepted), Mockito.any(Pageable.class)))
                .thenReturn(List.of(this.testUserTrainer, this.testUserTrainer2));
        List<TrainerUserInfoServiceModel> actual = this.userTrainerService.getTrainersPageForUserId(this.testUser.getId(), 0, 2, "rating", "asc");
        Assertions.assertEquals(2, actual.size(), "UserTrainer is not correct");
    }

    @Test
    void getTrainersPageForUserIdShouldReturnCorrectCustomSortingTrainingAsc() {
        Mockito.when(this.userRepository.findById(this.testUser.getId()))
                .thenReturn(Optional.of(this.testUser));
        Mockito.when(this.userTrainerRepository
                .getByTrainerIdAndUserId(this.testTrainer.getId(), this.testUser.getId()))
                .thenReturn(Optional.of(this.testUserTrainer));
        Mockito.when(this.userTrainerRepository
                .getAllTrainersUserIdSortedByTotalUsersAsc(this.testUser.getId(), 2L, 4L))
                .thenReturn(List.of(this.testUserTrainer));
        List<TrainerUserInfoServiceModel> actual = this.userTrainerService.getTrainersPageForUserId(this.testUser.getId(), 1, 2, "totalUsers", "asc");
        Assertions.assertEquals(1, actual.size(), "UserTrainer is not correct");
    }

    @Test
    void getTrainersPageForUserIdShouldReturnCorrectCustomSortingTrainingDesc() {
        Mockito.when(this.userRepository.findById(this.testUser.getId()))
                .thenReturn(Optional.of(this.testUser));
        Mockito.when(this.userTrainerRepository
                .getByTrainerIdAndUserId(this.testTrainer.getId(), this.testUser.getId()))
                .thenReturn(Optional.of(this.testUserTrainer));
        Mockito.when(this.userTrainerRepository
                .getAllTrainersUserIdSortedByTotalUsersDesc(this.testUser.getId(), 2L, 4L))
                .thenReturn(List.of(this.testUserTrainer));
        List<TrainerUserInfoServiceModel> actual = this.userTrainerService.getTrainersPageForUserId(this.testUser.getId(), 1, 2, "totalUsers", "desc");
        Assertions.assertEquals(1, actual.size(), "UserTrainer is not correct");
    }

    @Test
    void getTrainersPageForUserIdShouldReturnCorrectCustomSortingTrainingAsc2() {
        Mockito.when(this.userRepository.findById(this.testUser.getId()))
                .thenReturn(Optional.of(this.testUser));
        Mockito.when(this.userTrainerRepository
                .getByTrainerIdAndUserId(this.testTrainer.getId(), this.testUser.getId()))
                .thenReturn(Optional.of(this.testUserTrainer));
        Mockito.when(this.userTrainerRepository
                .getAllTrainersUserIdSortedByTotalUsersAsc(this.testUser.getId(), 2L, 4L))
                .thenReturn(List.of(this.testUserTrainer, this.testUserTrainer2));
        List<TrainerUserInfoServiceModel> actual = this.userTrainerService.getTrainersPageForUserId(this.testUser.getId(), 1, 2, "totalUsers", "asc");
        Assertions.assertEquals(2, actual.size(), "UserTrainer is not correct");
    }

    @Test
    void getTrainersPageForUserIdShouldReturnCorrectCustomSortingTrainingDesc2() {
        this.testTrainer.getUsers().add(new UserTrainer());
        this.testTrainer.getUsers().add(new UserTrainer());
        Mockito.when(this.userRepository.findById(this.testUser.getId()))
                .thenReturn(Optional.of(this.testUser));
        Mockito.when(this.userTrainerRepository
                .getByTrainerIdAndUserId(this.testTrainer.getId(), this.testUser.getId()))
                .thenReturn(Optional.of(this.testUserTrainer));
        Mockito.when(this.userTrainerRepository
                .getAllTrainersUserIdSortedByTotalUsersDesc(this.testUser.getId(), 2L, 4L))
                .thenReturn(List.of(this.testUserTrainer, this.testUserTrainer2));
        List<TrainerUserInfoServiceModel> actual = this.userTrainerService.getTrainersPageForUserId(this.testUser.getId(), 1, 2, "totalUsers", "desc");
        Assertions.assertEquals(2, actual.size(), "UserTrainer is not correct");
    }

    @Test
    void getTrainersPageForUserIdShouldReturnCorrectZeroRating() {
        Mockito.when(this.userRepository.findById(this.testUser.getId()))
                .thenReturn(Optional.of(this.testUser));
        Mockito.when(this.userTrainerRepository
                .getByTrainerIdAndUserId(this.testTrainer.getId(), this.testUser.getId()))
                .thenReturn(Optional.of(this.testUserTrainer));
        Mockito.when(this.userTrainerRepository
                .sumAvgRatingForTrainer(this.testTrainer.getId()))
                .thenReturn(Optional.empty());
        Mockito.when(this.userTrainerRepository
                .getAllByUserIdAndStatusType(Mockito.eq(this.testUser.getId()), Mockito.eq(RelationStatus.Accepted), Mockito.any(Pageable.class)))
                .thenReturn(List.of(this.testUserTrainer));
        List<TrainerUserInfoServiceModel> actual = this.userTrainerService.getTrainersPageForUserId(this.testUser.getId(), 1, 2, "rating", "desc");
        Assertions.assertEquals(0, actual.get(0).getRating(), "UserTrainer is not correct");
    }

    @Test
    void getTrainersPageForUserIdShouldReturnCorrectType() {
        Mockito.when(this.userRepository.findById(this.testUser.getId()))
                .thenReturn(Optional.of(this.testUser));
        Mockito.when(this.userTrainerRepository
                .getByTrainerIdAndUserId(this.testTrainer.getId(), this.testUser.getId()))
                .thenReturn(Optional.of(this.testUserTrainer));
        Mockito.when(this.userTrainerRepository
                .getAllByUserIdAndStatusType(Mockito.eq(this.testUser.getId()), Mockito.eq(RelationStatus.Accepted), Mockito.any(Pageable.class)))
                .thenReturn(List.of(this.testUserTrainer));
        List<TrainerUserInfoServiceModel> actual = this.userTrainerService.getTrainersPageForUserId(this.testUser.getId(), 1, 2, "rating", "desc");
        Assertions.assertEquals(this.testTrainer.getType(), actual.get(0).getType(), "UserTrainer is not correct");
    }

    @Test
    void getTrainersPageForUserIdShouldReturnCorrectRating() {
        this.testUserTrainer.setRating(5);
        Mockito.when(this.userRepository.findById(this.testUser.getId()))
                .thenReturn(Optional.of(this.testUser));
        Mockito.when(this.userTrainerRepository
                .getByTrainerIdAndUserId(this.testTrainer.getId(), this.testUser.getId()))
                .thenReturn(Optional.of(this.testUserTrainer));
        Mockito.when(this.userTrainerRepository
                .sumAvgRatingForTrainer(this.testTrainer.getId()))
                .thenReturn(Optional.of(5.0));
        Mockito.when(this.userTrainerRepository
                .getAllByUserIdAndStatusType(Mockito.eq(this.testUser.getId()), Mockito.eq(RelationStatus.Accepted), Mockito.any(Pageable.class)))
                .thenReturn(List.of(this.testUserTrainer));
        List<TrainerUserInfoServiceModel> actual = this.userTrainerService.getTrainersPageForUserId(this.testUser.getId(), 1, 2, "rating", "desc");
        Assertions.assertEquals(this.testUserTrainer.getRating() * 1.0, actual.get(0).getRating(), "UserTrainer is not correct");
    }

    @Test
    void getTrainersPageForUserIdShouldReturnCorrectUsername() {
        Mockito.when(this.userRepository.findById(this.testUser.getId()))
                .thenReturn(Optional.of(this.testUser));
        Mockito.when(this.userTrainerRepository
                .getByTrainerIdAndUserId(this.testTrainer.getId(), this.testUser.getId()))
                .thenReturn(Optional.of(this.testUserTrainer));
        Mockito.when(this.userTrainerRepository
                .getAllByUserIdAndStatusType(Mockito.eq(this.testUser.getId()), Mockito.eq(RelationStatus.Accepted), Mockito.any(Pageable.class)))
                .thenReturn(List.of(this.testUserTrainer));
        List<TrainerUserInfoServiceModel> actual = this.userTrainerService.getTrainersPageForUserId(this.testUser.getId(), 1, 2, "rating", "desc");
        Assertions.assertEquals(this.testUser.getUsername(), actual.get(0).getUsername(), "UserTrainer is not correct");
    }

    @Test
    void getTrainersPageForUserIdShouldReturnCorrectId() {
        Mockito.when(this.userRepository.findById(this.testUser.getId()))
                .thenReturn(Optional.of(this.testUser));
        Mockito.when(this.userTrainerRepository
                .getByTrainerIdAndUserId(this.testTrainer.getId(), this.testUser.getId()))
                .thenReturn(Optional.of(this.testUserTrainer));
        Mockito.when(this.userTrainerRepository
                .getAllByUserIdAndStatusType(Mockito.eq(this.testUser.getId()), Mockito.eq(RelationStatus.Accepted), Mockito.any(Pageable.class)))
                .thenReturn(List.of(this.testUserTrainer));
        List<TrainerUserInfoServiceModel> actual = this.userTrainerService.getTrainersPageForUserId(this.testUser.getId(), 1, 2, "rating", "desc");
        Assertions.assertEquals(this.testTrainer.getId(), actual.get(0).getId(), "UserTrainer is not correct");
    }

    @Test
    void getTrainersPageForWrongUserIdShouldThrowException() {
        Mockito.when(this.userRepository.findById(this.testUser.getId()))
                .thenReturn(Optional.of(this.testUser));
        Assertions.assertThrows(NotFoundException.class,
                () -> this.userTrainerService.getTrainersPageForUserId(this.testUser2.getId(), 1, 2, "age", "desc"),
                "UserTrainer is not correct");
    }

    @Test
    void getTrainersPageForWrongSortingTypeShouldThrowException() {
        Mockito.when(this.userRepository.findById(this.testUser.getId()))
                .thenReturn(Optional.of(this.testUser));
        Assertions.assertThrows(InvalidSortingTypeException.class,
                () -> this.userTrainerService.getTrainersPageForUserId(this.testUser.getId(), 1, 2, "agde", "desc"),
                "UserTrainer is not correct");
    }

    @Test
    void getTrainersPageForWrongSortingTypeShouldThrowException2() {
        Mockito.when(this.userRepository.findById(this.testUser.getId()))
                .thenReturn(Optional.of(this.testUser));
        Assertions.assertThrows(InvalidSortingTypeException.class,
                () -> this.userTrainerService.getTrainersPageForUserId(this.testUser.getId(), 1, 2, "age", "defdssc"),
                "UserTrainer is not correct");
    }

    @Test
    void getTrainersPageForWrongSortingTypeShouldThrowException3() {
        Mockito.when(this.userRepository.findById(this.testUser.getId()))
                .thenReturn(Optional.of(this.testUser));
        Assertions.assertThrows(InvalidSortingTypeException.class,
                () -> this.userTrainerService.getTrainersPageForUserId(this.testUser.getId(), 1, 2, "agde", "defdssc"),
                "UserTrainer is not correct");
    }

    /**
     * UserTrainerService checkIfTrainerHasUser(...) Tests
     */

    @Test
    void checkIfTrainerHasUserShouldReturnTrue() {
        Mockito.when(this.userTrainerRepository
                .getByTrainerIdAndUserIdAndStatusType(this.testTrainer.getId(), this.testUser.getId(), RelationStatus.Accepted))
                .thenReturn(Optional.of(this.testUserTrainer));
        Assertions.assertTrue(this.userTrainerService.checkIfTrainerHasUser(this.testTrainer.getId(), this.testUser.getId()), "UserTrainer is not correct");
    }

    @Test
    void checkIfTrainerHasUserShouldReturnFalse() {
        Mockito.when(this.userTrainerRepository
                .getByTrainerIdAndUserIdAndStatusType(this.testTrainer.getId(), this.testUser.getId(), RelationStatus.Accepted))
                .thenReturn(Optional.of(this.testUserTrainer));
        Assertions.assertFalse(this.userTrainerService.checkIfTrainerHasUser(this.testTrainer.getId(), this.testUser2.getId()), "UserTrainer is not correct");
    }

    @Test
    void checkIfTrainerHasUserShouldReturnFalse2() {
        Mockito.when(this.userTrainerRepository
                .getByTrainerIdAndUserIdAndStatusType(this.testTrainer.getId(), this.testUser.getId(), RelationStatus.Accepted))
                .thenReturn(Optional.of(this.testUserTrainer));
        Assertions.assertFalse(this.userTrainerService.checkIfTrainerHasUser(this.testTrainer2.getId(), this.testUser.getId()), "UserTrainer is not correct");
    }

    /**
     * UserTrainerService getRatingForTrainerId(...) Tests
     */

    @Test
    void getRatingForTrainerIdShouldReturnFive() {
        Mockito.when(this.userTrainerRepository
                .sumAvgRatingForTrainer(this.testTrainer.getId()))
                .thenReturn(Optional.of(5.0));
        Double actual = this.userTrainerService.getRatingForTrainerId(this.testTrainer.getId());
        Assertions.assertEquals(5.0, actual, "UserTrainer is not correct");
    }

    @Test
    void getRatingForTrainerIdShouldReturnZero() {
        Mockito.when(this.userTrainerRepository
                .sumAvgRatingForTrainer(this.testTrainer.getId()))
                .thenReturn(Optional.empty());
        Double actual = this.userTrainerService.getRatingForTrainerId(this.testTrainer.getId());
        Assertions.assertEquals(0.0, actual, "UserTrainer is not correct");
    }

    /**
     * UserTrainerService getTotalTrainerPagesCountForUserId(...) Tests
     */

    @Test
    void getTotalTrainerPagesCountForUserIdShouldReturnOne() {
        Mockito.when(this.userTrainerRepository
                .getAllByUserIdAndStatusType(this.testUser.getId(), RelationStatus.Accepted))
                .thenReturn(List.of(this.testUserTrainer2, this.testUserTrainer));
        Integer actual = this.userTrainerService.getTotalTrainerPagesCountForUserId(this.testUser.getId(), 2);
        Assertions.assertEquals(0, actual, "UserTrainer is not correct");
    }

    @Test
    void getTotalTrainerPagesCountForUserIdShouldReturnZero() {
        Mockito.when(this.userTrainerRepository
                .getAllByUserIdAndStatusType(this.testUser.getId(), RelationStatus.Accepted))
                .thenReturn(List.of(this.testUserTrainer2, this.testUserTrainer));
        Integer actual = this.userTrainerService.getTotalTrainerPagesCountForUserId(this.testUser.getId(), 3);
        Assertions.assertEquals(0, actual, "UserTrainer is not correct");
    }

    @Test
    void getTotalTrainerPagesCountForUserIdShouldReturnTwo() {
        Mockito.when(this.userTrainerRepository
                .getAllByUserIdAndStatusType(this.testUser.getId(), RelationStatus.Accepted))
                .thenReturn(List.of(this.testUserTrainer2, this.testUserTrainer, this.testUserTrainer3));
        Integer actual = this.userTrainerService.getTotalTrainerPagesCountForUserId(this.testUser.getId(), 2);
        Assertions.assertEquals(1, actual, "UserTrainer is not correct");
    }

    /**
     * UserTrainerService getTotalUserPagesCountForTrainerId(...) Tests
     */

    @Test
    void getTotalUserPagesCountForTrainerIdShouldReturnOne() {
        Mockito.when(this.userTrainerRepository
                .getAllByTrainerIdAndStatusType(this.testTrainer.getId(), RelationStatus.Accepted))
                .thenReturn(List.of(this.testUserTrainer2, this.testUserTrainer));
        Integer actual = this.userTrainerService.getTotalUserPagesCountForTrainerId(this.testTrainer.getId(), 2);
        Assertions.assertEquals(0, actual, "UserTrainer is not correct");
    }

    @Test
    void getTotalUserPagesCountForTrainerIdShouldReturnZero() {
        Mockito.when(this.userTrainerRepository
                .getAllByTrainerIdAndStatusType(this.testTrainer.getId(), RelationStatus.Accepted))
                .thenReturn(List.of(this.testUserTrainer2, this.testUserTrainer));
        Integer actual = this.userTrainerService.getTotalUserPagesCountForTrainerId(this.testTrainer.getId(), 3);
        Assertions.assertEquals(0, actual, "UserTrainer is not correct");
    }

    @Test
    void getTotalUserPagesCountForTrainerIdShouldReturnTwo() {
        Mockito.when(this.userTrainerRepository
                .getAllByTrainerIdAndStatusType(this.testTrainer.getId(), RelationStatus.Accepted))
                .thenReturn(List.of(this.testUserTrainer2, this.testUserTrainer, this.testUserTrainer3));
        Integer actual = this.userTrainerService.getTotalUserPagesCountForTrainerId(this.testTrainer.getId(), 2);
        Assertions.assertEquals(1, actual, "UserTrainer is not correct");
    }

    /**
     * UserTrainerService rateTrainer(...) Tests
     */

    @Test
    void rateTrainerShouldReturnThree() throws NoTrainerFoundForUserException {
        this.testUserTrainer.setRating(3);
        Mockito.when(this.userTrainerRepository
                .getByTrainerIdAndUserIdAndStatusType(this.testTrainer.getId(), this.testUser.getId(), RelationStatus.Accepted))
                .thenReturn(Optional.of(this.testUserTrainer));
        Mockito.when(this.userTrainerRepository
                .sumAvgRatingForTrainer(this.testTrainer.getId()))
                .thenReturn(Optional.of(this.testUserTrainer.getRating() * 1.0));
        RatingBindingModel model = new RatingBindingModel();
        model.setRating(3);
        RatingServiceModel actual = this.userTrainerService.rateTrainer(this.testUser.getId(), this.testTrainer.getId(), model);
        Assertions.assertEquals(this.testUserTrainer.getRating() * 1.0, actual.getRating(), "UserTrainer is not correct");
    }

    @Test
    void rateTrainerShouldReturnZero() throws NoTrainerFoundForUserException {
        Mockito.when(this.userTrainerRepository
                .getByTrainerIdAndUserIdAndStatusType(this.testTrainer.getId(), this.testUser.getId(), RelationStatus.Accepted))
                .thenReturn(Optional.of(this.testUserTrainer));
        Mockito.when(this.userTrainerRepository
                .sumAvgRatingForTrainer(this.testTrainer.getId()))
                .thenReturn(Optional.empty());
        RatingBindingModel model = new RatingBindingModel();
        RatingServiceModel actual = this.userTrainerService.rateTrainer(this.testUser.getId(), this.testTrainer.getId(), model);
        Assertions.assertEquals(0, actual.getRating(), "UserTrainer is not correct");
    }

    @Test
    void rateTrainerShouldSetRatingToEntity() throws NoTrainerFoundForUserException {
        this.testUserTrainer.setRating(0);
        Mockito.when(this.userTrainerRepository
                .getByTrainerIdAndUserIdAndStatusType(this.testTrainer.getId(), this.testUser.getId(), RelationStatus.Accepted))
                .thenReturn(Optional.of(this.testUserTrainer));
        Mockito.when(this.userTrainerRepository
                .sumAvgRatingForTrainer(this.testTrainer.getId()))
                .thenReturn(Optional.empty());
        RatingBindingModel model = new RatingBindingModel();
        model.setRating(3);
        this.userTrainerService.rateTrainer(this.testUser.getId(), this.testTrainer.getId(), model);
        Assertions.assertEquals(3, this.testUserTrainer.getRating(), "UserTrainer is not correct");
    }

    @Test
    void rateTrainerShouldThrowException() {
        Mockito.when(this.userTrainerRepository
                .getByTrainerIdAndUserIdAndStatusType(this.testTrainer.getId(), this.testUser.getId(), RelationStatus.Accepted))
                .thenReturn(Optional.of(this.testUserTrainer));
        RatingBindingModel model = new RatingBindingModel();
        Assertions.assertThrows(NoTrainerFoundForUserException.class,
                () -> this.userTrainerService.rateTrainer(this.testUser.getId(), this.testTrainer2.getId(), model),
                "UserTrainer is not correct");
    }
}
