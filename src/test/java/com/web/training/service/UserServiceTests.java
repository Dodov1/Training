package com.web.training.service;

import com.web.training.models.exceptionModels.NotFoundException;
import com.web.training.models.exceptionModels.UserAlreadyExistsException;
import com.web.training.models.exceptionModels.UserIsAlreadyTrainerException;
import com.web.training.models.bindingModels.AddTrainerRequestBindingModel;
import com.web.training.models.bindingModels.SearchSuggestionBindingModel;
import com.web.training.models.bindingModels.UserRegisterBindingModel;
import com.web.training.models.entities.Authority;
import com.web.training.models.entities.Picture;
import com.web.training.models.entities.Trainer;
import com.web.training.models.entities.User;
import com.web.training.models.enums.RelationStatus;
import com.web.training.models.enums.TrainerType;
import com.web.training.models.serviceModels.UserBasicPicServiceModel;
import com.web.training.models.serviceModels.UserProfileServiceModel;
import com.web.training.models.serviceModels.UserServiceModel;
import com.web.training.repositories.AuthorityRepository;
import com.web.training.repositories.PictureRepository;
import com.web.training.repositories.UserRepository;
import com.web.training.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static com.web.training.config.appConstants.AppConstants.DEFAULT_USER_AUTHORITY;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
public class UserServiceTests {

    @Mock
    private UserRepository userRepository;
    @Mock
    private AuthorityRepository authorityRepository;
    @Mock
    private PictureRepository pictureRepository;
    @Mock
    private ApplicationEventPublisher publisher;
    @Mock
    private TrainerService trainerService;


    private UserService userService;

    private static final Long CORRECT_USER_ID = 1L;
    private static final String CORRECT_USER_PIC_LOCATION_ONE = "asd1";
    private static final String CORRECT_USER_PIC_LOCATION_TWO = "asd2";
    private static final Long WRONG_USER_ID = 2L;
    private static final String WRONG_USER_USERNAME = "demo1";
    private static final String WRONG_USER_EMAIL = "demo1";

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private final ModelMapper modelMapper = new ModelMapper();

    private User testUser;
    private User testUser2;
    private User testUser3;
    private User testUserToRegister;
    private Picture testPicOne;
    private Picture testPicTwo;
    private SearchSuggestionBindingModel model;

    @BeforeEach
    public void init() {
        Trainer trainer = new Trainer();
        trainer.setType(TrainerType.Group);
        trainer.setId(1L);
        trainer.setStatus(RelationStatus.Accepted);
        trainer.setUser(this.testUser);
        trainer.setUsers(new HashSet<>());
        this.testUser = new User() {{
            setId(1L);
            setEnabled(true);
            setTrainer(trainer);
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

        Mockito.when(this.userRepository
                .findById(CORRECT_USER_ID))
                .thenReturn(Optional.of(this.testUser));

        this.userService = new UserServiceImpl(this.userRepository, this.trainerService, this.authorityRepository, this.passwordEncoder, this.modelMapper, this.pictureRepository, publisher);
    }

    void initAllUsers() {
        this.testPicTwo = new Picture();
        this.testPicTwo.setLocation(CORRECT_USER_PIC_LOCATION_TWO);
        this.testPicOne = new Picture();
        this.testPicOne.setLocation(CORRECT_USER_PIC_LOCATION_ONE);
        this.testUser2 = new User() {{
            setId(2L);
            setEnabled(true);
            setTrainers(new HashSet<>());
            setTrainings(new HashSet<>());
            setWeights(new HashSet<>());
            setFirstName("PeshoF222");
            setLastName("PeshoL222");
            setUsername("PeshoU222");
            setEmail("Pesho222@mail.bg");
            setAge(8);
            setPicture(testPicOne);
            setPassword(passwordEncoder.encode("asdfg"));
            setHeight(164);
        }};
        this.testUser3 = new User() {{
            setId(3L);
            setEnabled(true);
            setPicture(testPicTwo);
            setTrainers(new HashSet<>());
            setTrainings(new HashSet<>());
            setWeights(new HashSet<>());
            setFirstName("PeshoF333");
            setLastName("PeshoL333");
            setUsername("PeshoU333");
            setEmail("Pesho333@mail.bg");
            setAge(8);
            setPassword(passwordEncoder.encode("asdfg"));
            setHeight(164);
        }};
        List<Long> notIds = new ArrayList<>();
        notIds.add(this.testUser.getId());
        Mockito.when(
                this.userRepository.getTop5ByUsernameStartsWithAndIdNotIn(this.testUser.getUsername(), notIds)
        ).thenReturn(List.of(this.testUser2, this.testUser3));
        this.model = new SearchSuggestionBindingModel();
        model.setNotIds(notIds);
    }

    void initUserToRegister() {
        this.testPicOne = new Picture();
        this.testPicOne.setLocation(CORRECT_USER_PIC_LOCATION_ONE);
        this.testUserToRegister = new User() {{
            setId(2L);
            setEnabled(true);
            setTrainers(new HashSet<>());
            setTrainings(new HashSet<>());
            setAuthorities(new ArrayList<>());
            setWeights(new HashSet<>());
            setFirstName("PeshoF222");
            setLastName("PeshoL222");
            setUsername("PeshoU222");
            setEmail("Pesho222@mail.bg");
            setAge(8);
            setPicture(testPicOne);
            setPassword(passwordEncoder.encode("asdfg"));
            setHeight(164);
        }};
        Authority testAuthority = new Authority() {{
            setId(1L);
            setUser(testUserToRegister);
            setName(DEFAULT_USER_AUTHORITY);
        }};
        Mockito.when(this.userRepository.saveAndFlush(any(User.class)))
                .thenReturn(this.testUserToRegister);
        Mockito.when(this.authorityRepository.saveAndFlush(any(Authority.class)))
                .thenReturn(testAuthority);
        Mockito.when(this.userRepository.findByUsername(this.testUserToRegister.getUsername()))
                .thenReturn(Optional.empty());
        Mockito.when(this.userRepository.findByUsername(this.testUser.getUsername()))
                .thenReturn(Optional.of(this.testUser));
        Mockito.when(this.userRepository.findByEmail(this.testUser.getEmail()))
                .thenReturn(Optional.of(this.testUser));

    }

    /**
     * UserService getUserById(...) Tests
     */

    @Test
    void getUserByIdShouldReturnCorrectId() {
        UserServiceModel actual = this.userService.getUserById(CORRECT_USER_ID);
        Assertions.assertEquals(this.testUser.getId(), actual.getId(), "User is not correct");
    }

    @Test
    void getUserByIdShouldReturnCorrectUsername() {
        UserServiceModel actual = this.userService.getUserById(CORRECT_USER_ID);
        Assertions.assertEquals(this.testUser.getUsername(), actual.getUsername(), "User is not correct");
    }

    @Test
    void getUserByIdShouldReturnCorrectEmail() {
        UserServiceModel actual = this.userService.getUserById(CORRECT_USER_ID);
        Assertions.assertEquals(this.testUser.getEmail(), actual.getEmail(), "User is not correct");
    }

    @Test
    void getUserByIdShouldReturnCorrectFirstName() {
        UserServiceModel actual = this.userService.getUserById(CORRECT_USER_ID);
        Assertions.assertEquals(this.testUser.getFirstName(), actual.getFirstName(), "User is not correct");
    }

    @Test
    void getUserByIdShouldReturnCorrectLastName() {
        UserServiceModel actual = this.userService.getUserById(CORRECT_USER_ID);
        Assertions.assertEquals(this.testUser.getLastName(), actual.getLastName(), "User is not correct");
    }

    @Test
    void getUserByIdShouldReturnCorrectAge() {
        UserServiceModel actual = this.userService.getUserById(CORRECT_USER_ID);
        Assertions.assertEquals(this.testUser.getAge(), actual.getAge(), "User is not correct");
    }

    @Test
    void getUserByIdShouldReturnCorrectTrainerStatus() {
        UserServiceModel actual = this.userService.getUserById(CORRECT_USER_ID);
        Assertions.assertEquals(this.testUser.getTrainer().getStatus(), actual.getTrainerStatus(), "User is not correct");
    }

    @Test
    void getUserByIdShouldReturnCorrectNullTrainerStatus() {
        this.testUser.setTrainer(null);
        UserServiceModel actual = this.userService.getUserById(CORRECT_USER_ID);
        Assertions.assertNull(actual.getTrainerStatus(), "User is not correct");
    }

    @Test
    void getUserByIdShouldReturnCorrectHeight() {
        UserServiceModel actual = this.userService.getUserById(CORRECT_USER_ID);
        Assertions.assertEquals(this.testUser.getHeight(), actual.getHeight(), "User is not correct");
    }

    @Test
    void getUserByWrongIdShouldThrowException() {
        assertThrows(NotFoundException.class, () -> this.userService.getUserById(WRONG_USER_ID));
    }

    /**
     * UserService checkIfUsernameExists(...) Tests
     */

    @Test
    void checkIfUsernameExistsShouldTrue() {
        Mockito.when(this.userRepository
                .findByUsername(this.testUser.getUsername()))
                .thenReturn(Optional.of(this.testUser));
        boolean actual = this.userService.checkIfUsernameExists(this.testUser.getUsername());
        Assertions.assertTrue(actual, "User is not correct");
    }

    @Test
    void checkIfUsernameExistsShouldFalse() {
        Mockito.when(this.userRepository
                .findByUsername(this.testUser.getUsername()))
                .thenReturn(Optional.of(this.testUser));
        boolean actual = this.userService.checkIfUsernameExists(WRONG_USER_USERNAME);
        Assertions.assertFalse(actual, "User is not correct");
    }

    /**
     * UserService checkIfEmailExists(...) Tests
     */

    @Test
    void checkIfEmailExistsShouldTrue() {
        Mockito.when(this.userRepository
                .findByEmail(this.testUser.getEmail()))
                .thenReturn(Optional.of(this.testUser));
        boolean actual = this.userService.checkIfEmailExists(this.testUser.getEmail());
        Assertions.assertTrue(actual, "User is not correct");
    }

    @Test
    void checkIfEmailExistsShouldFalse() {
        Mockito.when(this.userRepository
                .findByEmail(this.testUser.getEmail()))
                .thenReturn(Optional.of(this.testUser));
        boolean actual = this.userService.checkIfEmailExists(WRONG_USER_EMAIL);
        Assertions.assertFalse(actual, "User is not correct");
    }

    /**
     * UserService getUserProfileById(...) Tests
     */

    @Test
    void getUserProfileByIdShouldReturnCorrectId() {
        UserProfileServiceModel actual = this.userService.getUserProfileById(this.testUser.getId());
        Assertions.assertEquals(this.testUser.getId(), actual.getId(), "User is not correct");
    }

    @Test
    void getUserProfileByIdShouldReturnCorrectUsername() {
        UserProfileServiceModel actual = this.userService.getUserProfileById(this.testUser.getId());
        Assertions.assertEquals(this.testUser.getUsername(), actual.getUsername(), "User is not correct");
    }

    @Test
    void getUserProfileByIdShouldReturnCorrectEmail() {
        UserProfileServiceModel actual = this.userService.getUserProfileById(this.testUser.getId());
        Assertions.assertEquals(this.testUser.getEmail(), actual.getEmail(), "User is not correct");
    }

    @Test
    void getUserProfileByIdShouldReturnCorrectFirstName() {
        UserProfileServiceModel actual = this.userService.getUserProfileById(this.testUser.getId());
        Assertions.assertEquals(this.testUser.getFirstName(), actual.getFirstName(), "User is not correct");
    }

    @Test
    void getUserProfileByIdShouldReturnCorrectLastName() {
        UserProfileServiceModel actual = this.userService.getUserProfileById(this.testUser.getId());
        Assertions.assertEquals(this.testUser.getLastName(), actual.getLastName(), "User is not correct");
    }

    @Test
    void getUserProfileByIdShouldReturnCorrectAge() {
        UserProfileServiceModel actual = this.userService.getUserProfileById(this.testUser.getId());
        Assertions.assertEquals(this.testUser.getAge(), actual.getAge(), "User is not correct");
    }

    @Test
    void getUserProfileByIdShouldReturnCorrectHeight() {
        UserProfileServiceModel actual = this.userService.getUserProfileById(this.testUser.getId());
        Assertions.assertEquals(this.testUser.getHeight(), actual.getHeight(), "User is not correct");
    }

    @Test
    void getUserProfileByIdShouldReturnCorrectStatusType() {
        UserProfileServiceModel actual = this.userService.getUserProfileById(this.testUser.getId());
        Assertions.assertNull(actual.getStatusType(), "User is not correct");
    }

    @Test
    void getUserProfileByIdShouldThrowException() {
        Assertions.assertThrows(NotFoundException.class, () -> this.userService.getUserProfileById(WRONG_USER_ID));
    }

    /**
     * UserService checkIfUserExistsById(...) Tests
     */

    @Test
    void checkIfUserExistsByIdShouldTrue() {
        boolean actual = this.userService.checkIfUserExistsById(this.testUser.getId());
        Assertions.assertTrue(actual, "User is not correct");
    }

    @Test
    void checkIfUserExistsByIdShouldFalse() {
        boolean actual = this.userService.checkIfUserExistsById(WRONG_USER_ID);
        Assertions.assertFalse(actual, "User is not correct");
    }

    /**
     * UserService requestToBecomeTrainer(...) Tests
     */

    @Test
    void requestToBecomeTrainerShouldTrue() throws UserIsAlreadyTrainerException {
        AddTrainerRequestBindingModel model = new AddTrainerRequestBindingModel();
        boolean actual = this.userService.requestToBecomeTrainer(this.testUser.getId(), model);
        Assertions.assertTrue(actual, "User is not correct");
    }

    @Test
    void requestToBecomeTrainerShouldThrowException() {
        AddTrainerRequestBindingModel model = new AddTrainerRequestBindingModel();
        Assertions.assertThrows(NotFoundException.class, () -> this.userService.requestToBecomeTrainer(WRONG_USER_ID, model));
    }

    /**
     * UserService getSuggestionsByUsername(...) Tests
     */

    @Test
    void getSuggestionsByUsernameShouldReturnEmptyList() {
        this.initAllUsers();
        model.setInput(WRONG_USER_USERNAME);
        List<UserBasicPicServiceModel> actual = this.userService.getSuggestionsByUsername(model);
        Assertions.assertEquals(0, actual.size(), "User is not correct");
    }

    @Test
    void getSuggestionsByUsernameShouldReturnListWithSizeTwo() {
        this.initAllUsers();
        model.setInput(this.testUser.getUsername());
        List<UserBasicPicServiceModel> actual = this.userService.getSuggestionsByUsername(model);
        Assertions.assertEquals(2, actual.size(), "User is not correct");
    }

    @Test
    void getSuggestionsByUsernameShouldReturnListWithSizeTwoWithNotIdsZeroCount() {
        this.initAllUsers();
        SearchSuggestionBindingModel model = new SearchSuggestionBindingModel();
        Mockito.when(this.userRepository.getTop5ByUsernameStartsWith(this.testUser.getUsername()))
                .thenReturn(List.of(this.testUser2, this.testUser3));
        model.setInput(this.testUser.getUsername());
        model.setNotIds(new ArrayList<>());
        List<UserBasicPicServiceModel> actual = this.userService.getSuggestionsByUsername(model);
        Assertions.assertEquals(2, actual.size(), "User is not correct");
    }

    @Test
    void getSuggestionsByWrongUsernameShouldReturnListWithSizeZeroCount() {
        this.initAllUsers();
        SearchSuggestionBindingModel model = new SearchSuggestionBindingModel();
        Mockito.when(this.userRepository.getTop5ByUsernameStartsWith(this.testUser.getUsername()))
                .thenReturn(List.of(this.testUser2, this.testUser3));
        model.setInput(WRONG_USER_USERNAME);
        model.setNotIds(new ArrayList<>());
        List<UserBasicPicServiceModel> actual = this.userService.getSuggestionsByUsername(model);
        Assertions.assertEquals(0, actual.size(), "User is not correct");
    }

    @Test
    void getSuggestionsByUsernameShouldReturnListElementOneCorrectId() {
        this.initAllUsers();
        model.setInput(this.testUser.getUsername());
        List<UserBasicPicServiceModel> actual = this.userService.getSuggestionsByUsername(model);
        Assertions.assertEquals(this.testUser2.getId(), actual.get(0).getId(), "User is not correct");
    }

    @Test
    void getSuggestionsByUsernameShouldReturnListElementOneCorrectUsername() {
        this.initAllUsers();
        model.setInput(this.testUser.getUsername());
        List<UserBasicPicServiceModel> actual = this.userService.getSuggestionsByUsername(model);
        Assertions.assertEquals(this.testUser2.getUsername(), actual.get(0).getUsername(), "User is not correct");
    }

    @Test
    void getSuggestionsByUsernameShouldReturnListElementOneCorrectFirstName() {
        this.initAllUsers();
        model.setInput(this.testUser.getUsername());
        List<UserBasicPicServiceModel> actual = this.userService.getSuggestionsByUsername(model);
        Assertions.assertEquals(this.testUser2.getFirstName(), actual.get(0).getFirstName(), "User is not correct");
    }

    @Test
    void getSuggestionsByUsernameShouldReturnListElementOneCorrectLastName() {
        this.initAllUsers();
        model.setInput(this.testUser.getUsername());
        List<UserBasicPicServiceModel> actual = this.userService.getSuggestionsByUsername(model);
        Assertions.assertEquals(this.testUser2.getLastName(), actual.get(0).getLastName(), "User is not correct");
    }

    @Test
    void getSuggestionsByUsernameShouldReturnListElementOneCorrectProfilePicture() {
        this.initAllUsers();
        model.setInput(this.testUser.getUsername());
        List<UserBasicPicServiceModel> actual = this.userService.getSuggestionsByUsername(model);
        Assertions.assertEquals(this.testUser2.getPicture().getLocation(), actual.get(0).getProfilePicture(), "User is not correct");
    }

    /**
     * UserService enableUserById(...) Tests
     */

    @Test
    void enableUserByIdShouldReturnEnabledTrue() {
        boolean actual = this.userService.enableUserById(CORRECT_USER_ID);
        Assertions.assertTrue(actual, "User is not correct");
    }

    @Test
    void enableUserByIdShouldReturnUserEnabledTrue() {
        this.testUser.setEnabled(false);
        this.userService.enableUserById(CORRECT_USER_ID);
        Assertions.assertTrue(this.testUser.getEnabled(), "User is not correct");
    }

    @Test
    void enableUserByIdShouldThrowException() {
        Assertions.assertThrows(NotFoundException.class, () -> this.userService.enableUserById(WRONG_USER_ID));
    }

    /**
     * UserService addNewUser(...) Tests
     */

    @Test
    void addNewUserShouldReturnCorrectAge() {
        this.initUserToRegister();
        UserRegisterBindingModel register = this.modelMapper.map(this.testUserToRegister, UserRegisterBindingModel.class);
        UserServiceModel userServiceModel = this.userService.addNewUser(register);
        Assertions.assertEquals(this.testUserToRegister.getAge(), userServiceModel.getAge(), "User is not correct");
    }

    @Test
    void addNewUserShouldReturnCorrectHeight() {
        this.initUserToRegister();
        UserRegisterBindingModel register = this.modelMapper.map(this.testUserToRegister, UserRegisterBindingModel.class);
        UserServiceModel userServiceModel = this.userService.addNewUser(register);
        Assertions.assertEquals(this.testUserToRegister.getHeight(), userServiceModel.getHeight(), "User is not correct");
    }

    @Test
    void addNewUserShouldReturnCorrectFirstName() {
        this.initUserToRegister();
        UserRegisterBindingModel register = this.modelMapper.map(this.testUserToRegister, UserRegisterBindingModel.class);
        UserServiceModel userServiceModel = this.userService.addNewUser(register);
        Assertions.assertEquals(this.testUserToRegister.getFirstName(), userServiceModel.getFirstName(), "User is not correct");
    }

    @Test
    void addNewUserShouldReturnCorrectLastName() {
        this.initUserToRegister();
        UserRegisterBindingModel register = this.modelMapper.map(this.testUserToRegister, UserRegisterBindingModel.class);
        UserServiceModel userServiceModel = this.userService.addNewUser(register);
        Assertions.assertEquals(this.testUserToRegister.getLastName(), userServiceModel.getLastName(), "User is not correct");
    }

    @Test
    void addNewUserShouldReturnCorrectUsername() {
        this.initUserToRegister();
        UserRegisterBindingModel register = this.modelMapper.map(this.testUserToRegister, UserRegisterBindingModel.class);
        UserServiceModel userServiceModel = this.userService.addNewUser(register);
        Assertions.assertEquals(this.testUserToRegister.getUsername(), userServiceModel.getUsername(), "User is not correct");
    }

    @Test
    void addNewUserShouldReturnCorrectEmail() {
        this.initUserToRegister();
        UserRegisterBindingModel register = this.modelMapper.map(this.testUserToRegister, UserRegisterBindingModel.class);
        UserServiceModel userServiceModel = this.userService.addNewUser(register);
        Assertions.assertEquals(this.testUserToRegister.getEmail(), userServiceModel.getEmail(), "User is not correct");
    }

    @Test
    void addNewUserShouldReturnCorrectStatus() {
        this.initUserToRegister();
        UserRegisterBindingModel register = this.modelMapper.map(this.testUserToRegister, UserRegisterBindingModel.class);
        UserServiceModel userServiceModel = this.userService.addNewUser(register);
        Assertions.assertNull(userServiceModel.getTrainerStatus(), "User is not correct");
    }

    @Test
    void addNewUserShouldReturnCorrectAuthoritiesCount() {
        this.initUserToRegister();
        UserRegisterBindingModel register = this.modelMapper.map(this.testUserToRegister, UserRegisterBindingModel.class);
        this.userService.addNewUser(register);
        Assertions.assertEquals(1, this.testUserToRegister.getAuthorities().size(), "User is not correct");
    }

    @Test
    void addNewUserShouldReturnCorrectUserAuthority() {
        this.initUserToRegister();
        UserRegisterBindingModel register = this.modelMapper.map(this.testUserToRegister, UserRegisterBindingModel.class);
        this.userService.addNewUser(register);
        Assertions.assertEquals(DEFAULT_USER_AUTHORITY, this.testUserToRegister.getAuthorities().get(0).getName(), "User is not correct");
    }

    @Test
    void addNewUserShouldThrowExceptionUsernameDuplicate() {
        this.initUserToRegister();
        this.testUser.setEmail("ads");
        UserRegisterBindingModel register = this.modelMapper.map(this.testUser, UserRegisterBindingModel.class);
        Assertions.assertThrows(UserAlreadyExistsException.class, () -> this.userService.addNewUser(register), "User is not correct");
    }

    @Test
    void addNewUserShouldThrowExceptionEmailDuplicate() {
        this.initUserToRegister();
        this.testUser.setUsername("ads");
        UserRegisterBindingModel register = this.modelMapper.map(this.testUser, UserRegisterBindingModel.class);
        Assertions.assertThrows(UserAlreadyExistsException.class, () -> this.userService.addNewUser(register), "User is not correct");
    }

    @Test
    void addNewUserShouldThrowExceptionEmailAndUsernameDuplicate() {
        this.initUserToRegister();
        UserRegisterBindingModel register = this.modelMapper.map(this.testUser, UserRegisterBindingModel.class);
        Assertions.assertThrows(UserAlreadyExistsException.class, () -> this.userService.addNewUser(register), "User is not correct");
    }
}
