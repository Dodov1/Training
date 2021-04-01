package com.web.training.web;

import com.web.training.models.exceptionModels.TrainerNotFoundException;
import com.web.training.models.enums.RelationStatus;
import com.web.training.models.serviceModels.*;
import com.web.training.service.TrainerService;
import com.web.training.service.UserTrainerService;
import com.web.training.service.WorkoutService;
import com.web.training.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserServiceImpl userService;
    @MockBean
    private UserTrainerService userTrainerService;
    @MockBean
    private TrainerService trainerService;
    @MockBean
    private WorkoutService workoutService;

    private UserServiceModel model;

    private static final Long CORRECT_USER_ID = 1L;
    private static final String CORRECT_USER_USERNAME = "demo";
    private static final String WRONG_USER_ID = "ASD";

    @BeforeEach
    public void init() {
        this.model = new UserServiceModel();
        this.model.setId(1L);
        this.model.setTrainerStatus(RelationStatus.Accepted);
        this.model.setAge(14);
        this.model.setUsername(".now()");
        this.model.setLastName(".now()Lname");
        this.model.setFirstName(".now()Fname");
        this.model.setEmail(".now()@asd");
        this.model.setHeight(165);
    }

    /**
     * UserController - getUserById(...) TESTS
     */

    @Test
    @WithMockUser(username = CORRECT_USER_USERNAME)
    public void getUserByIdShouldReturnCorrect() throws Exception {
        Mockito.when(this.userService.getUserById(CORRECT_USER_ID))
                .thenReturn(model);

        mockMvc.perform(get("/users/{id}", CORRECT_USER_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.age", is(model.getAge())))
                .andExpect(jsonPath("$.height", is(model.getHeight())))
                .andExpect(jsonPath("$.trainerStatus", is(model.getTrainerStatus().toString())))
                .andExpect(jsonPath("$.firstName", is(model.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(model.getLastName())))
                .andExpect(jsonPath("$.email", is(model.getEmail())))
                .andExpect(jsonPath("$.username", is(model.getUsername())));
    }

    @Test
    @WithMockUser(username = CORRECT_USER_USERNAME)
    public void getCurrentWeightForUserIdShouldReturnBadRequest() throws Exception {
        mockMvc.perform(get("/users/{id}/", WRONG_USER_ID))
                .andExpect(status().is(HttpStatus.BAD_REQUEST.value()));
    }

    /**
     * UserController - getTrainersByUserId(...) TESTS
     */

    @Test
    @WithMockUser(username = CORRECT_USER_USERNAME)
    public void getTrainersByUserIdShouldReturnCorrect() throws Exception {
        int page = 0;
        int size = 1;
        String sortBy = "id";
        String orderBy = "asc";
        Mockito.when(this.userTrainerService.getTrainersPageForUserId(CORRECT_USER_ID, page, size, sortBy, orderBy))
                .thenReturn(List.of(new TrainerUserInfoServiceModel()));

        mockMvc.perform(get("/users/{userId}/trainers/?page={page}&size={size}&sortBy={sortBy}&orderBy={orderBy}",
                CORRECT_USER_ID, page, size, sortBy, orderBy
        ))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.trainers", hasSize(1)))
                .andExpect(jsonPath("$._links", hasKey("self")))
                .andExpect(jsonPath("$._links", hasKey("sortByTypeAsc")))
                .andExpect(jsonPath("$._links", hasKey("sortByTypeDesc")))
                .andExpect(jsonPath("$._links", hasKey("sortByRatingAsc")))
                .andExpect(jsonPath("$._links", hasKey("sortByRatingDesc")))
                .andExpect(jsonPath("$._links", hasKey("sortByTotalUsersAsc")))
                .andExpect(jsonPath("$._links", hasKey("sortByTotalUsersDesc")));
    }

    @Test
    @WithMockUser(username = CORRECT_USER_USERNAME)
    public void getTrainersByUserIdShouldReturnCorrectLinks() throws Exception {
        int page = 0;
        int size = 1;
        String sortBy = "id";
        String orderBy = "asc";
        Mockito.when(this.userTrainerService.getTrainersPageForUserId(CORRECT_USER_ID, page, size, sortBy, orderBy))
                .thenReturn(List.of(new TrainerUserInfoServiceModel(), new TrainerUserInfoServiceModel()));

        Mockito.when(this.userTrainerService.getTotalTrainerPagesCountForUserId(CORRECT_USER_ID, size))
                .thenReturn(1);


        mockMvc.perform(get("/users/{userId}/trainers/?page={page}&size={size}&sortBy={sortBy}&orderBy={orderBy}",
                CORRECT_USER_ID, page, size, sortBy, orderBy
        ))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.trainers", hasSize(2)))
                .andExpect(jsonPath("$._links", hasKey("self")))
                .andExpect(jsonPath("$._links", hasKey("next")))
                .andExpect(jsonPath("$._links", hasKey("sortByTypeAsc")))
                .andExpect(jsonPath("$._links", hasKey("sortByTypeDesc")))
                .andExpect(jsonPath("$._links", hasKey("sortByRatingAsc")))
                .andExpect(jsonPath("$._links", hasKey("sortByRatingDesc")))
                .andExpect(jsonPath("$._links", hasKey("sortByTotalUsersAsc")))
                .andExpect(jsonPath("$._links", hasKey("sortByTotalUsersDesc")));
    }

    @Test
    @WithMockUser(username = CORRECT_USER_USERNAME)
    public void getTrainersByUserIdShouldReturnCorrectLinksPage2() throws Exception {
        int page = 2;
        int size = 1;
        String sortBy = "id";
        String orderBy = "asc";
        Mockito.when(this.userTrainerService.getTrainersPageForUserId(CORRECT_USER_ID, page, size, sortBy, orderBy))
                .thenReturn(List.of(new TrainerUserInfoServiceModel(), new TrainerUserInfoServiceModel()));

        Mockito.when(this.userTrainerService.getTotalTrainerPagesCountForUserId(CORRECT_USER_ID, size))
                .thenReturn(2);


        mockMvc.perform(get("/users/{userId}/trainers/?page={page}&size={size}&sortBy={sortBy}&orderBy={orderBy}",
                CORRECT_USER_ID, page, size, sortBy, orderBy
        ))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.trainers", hasSize(2)))
                .andExpect(jsonPath("$._links", hasKey("self")))
                .andExpect(jsonPath("$._links", hasKey("first")))
                .andExpect(jsonPath("$._links", hasKey("last")))
                .andExpect(jsonPath("$._links", hasKey("prev")))
                .andExpect(jsonPath("$._links", hasKey("sortByTypeAsc")))
                .andExpect(jsonPath("$._links", hasKey("sortByTypeDesc")))
                .andExpect(jsonPath("$._links", hasKey("sortByRatingAsc")))
                .andExpect(jsonPath("$._links", hasKey("sortByRatingDesc")))
                .andExpect(jsonPath("$._links", hasKey("sortByTotalUsersAsc")))
                .andExpect(jsonPath("$._links", hasKey("sortByTotalUsersDesc")));
    }

    @Test
    @WithMockUser(username = CORRECT_USER_USERNAME)
    public void getTrainersByUserIdShouldReturnCorrectLinksPage3() throws Exception {
        int page = 2;
        int size = 1;
        String sortBy = "id";
        String orderBy = "asc";
        Mockito.when(this.userTrainerService.getTrainersPageForUserId(CORRECT_USER_ID, page, size, sortBy, orderBy))
                .thenReturn(List.of(new TrainerUserInfoServiceModel(), new TrainerUserInfoServiceModel()));

        Mockito.when(this.userTrainerService.getTotalTrainerPagesCountForUserId(CORRECT_USER_ID, size))
                .thenReturn(3);


        mockMvc.perform(get("/users/{userId}/trainers/?page={page}&size={size}&sortBy={sortBy}&orderBy={orderBy}",
                CORRECT_USER_ID, page, size, sortBy, orderBy
        ))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.trainers", hasSize(2)))
                .andExpect(jsonPath("$._links", hasKey("self")))
                .andExpect(jsonPath("$._links", hasKey("first")))
                .andExpect(jsonPath("$._links", hasKey("next")))
                .andExpect(jsonPath("$._links", hasKey("last")))
                .andExpect(jsonPath("$._links", hasKey("prev")))
                .andExpect(jsonPath("$._links", hasKey("sortByTypeAsc")))
                .andExpect(jsonPath("$._links", hasKey("sortByTypeDesc")))
                .andExpect(jsonPath("$._links", hasKey("sortByRatingAsc")))
                .andExpect(jsonPath("$._links", hasKey("sortByRatingDesc")))
                .andExpect(jsonPath("$._links", hasKey("sortByTotalUsersAsc")))
                .andExpect(jsonPath("$._links", hasKey("sortByTotalUsersDesc")));
    }

    @Test
    @WithMockUser(username = CORRECT_USER_USERNAME)
    public void getTrainersByUserIdShouldThrowBadRequest() throws Exception {
        int page = 0;
        int size = 0;
        String sortBy = "id";
        String orderBy = "asc";
        Mockito.when(this.userTrainerService.getTrainersPageForUserId(CORRECT_USER_ID, page, size, sortBy, orderBy))
                .thenReturn(List.of(new TrainerUserInfoServiceModel()));

        mockMvc.perform(get("/users/{userId}/trainers/?page={page}&size={size}&sortBy={sortBy}",
                CORRECT_USER_ID, page, size, sortBy
        ))
                .andExpect(status().is(HttpStatus.BAD_REQUEST.value()));
    }

    @Test
    @WithMockUser(username = CORRECT_USER_USERNAME)
    public void getTrainersByUserIdShouldReturnBadRequest() throws Exception {
        mockMvc.perform(get("/users/{userId}/trainers", WRONG_USER_ID))
                .andExpect(status().is(HttpStatus.BAD_REQUEST.value()));
    }

    /**
     * UserController - getTrainerByUserIdAndTrainerId(...) TESTS
     */

    @Test
    @WithMockUser(username = CORRECT_USER_USERNAME)
    public void getTrainerByUserIdAndTrainerIdShouldReturnCorrect() throws Exception {
        Long trainerId = 2L;
        UserProfileServiceModel modelT = new UserProfileServiceModel();
        modelT.setId(1L);
        modelT.setStatusType(RelationStatus.NotAnswered);
        modelT.setUsername("ädsads");
        modelT.setFirstName("1L");
        modelT.setLastName("1L");
        modelT.setAge(13);
        modelT.setHeight(163);
        Mockito.when(this.userTrainerService.getTrainerByIdForUser(CORRECT_USER_ID, trainerId))
                .thenReturn(modelT);

        mockMvc.perform(get("/users/{userId}/trainers/{trainerId}", CORRECT_USER_ID, trainerId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.age", is(modelT.getAge())))
                .andExpect(jsonPath("$.height", is(modelT.getHeight())))
                .andExpect(jsonPath("$.firstName", is(modelT.getFirstName())))
                .andExpect(jsonPath("$.statusType", is(modelT.getStatusType().toString())))
                .andExpect(jsonPath("$.lastName", is(modelT.getLastName())))
                .andExpect(jsonPath("$.email", is(modelT.getEmail())))
                .andExpect(jsonPath("$.username", is(modelT.getUsername())));
    }

    @Test
    @WithMockUser(username = CORRECT_USER_USERNAME)
    public void getTrainerByUserIdAndTrainerIdShouldReturnWrong() throws Exception {
        Long trainerId = 2L;
        UserProfileServiceModel modelT = new UserProfileServiceModel();
        modelT.setId(1L);
        modelT.setStatusType(RelationStatus.NotAnswered);
        modelT.setUsername("ädsads");
        modelT.setFirstName("1L");
        modelT.setLastName("1L");
        modelT.setAge(13);
        modelT.setHeight(163);
        Mockito.when(this.userTrainerService.getTrainerByIdForUser(CORRECT_USER_ID, trainerId))
                .thenThrow(new TrainerNotFoundException(""));

        Mockito.when(this.trainerService.getTrainerProfileById(trainerId))
                .thenReturn(modelT);

        mockMvc.perform(get("/users/{userId}/trainers/{trainerId}", CORRECT_USER_ID, trainerId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.age", is(modelT.getAge())))
                .andExpect(jsonPath("$.height", is(modelT.getHeight())))
                .andExpect(jsonPath("$.firstName", is(modelT.getFirstName())))
                .andExpect(jsonPath("$.statusType", is(modelT.getStatusType().toString())))
                .andExpect(jsonPath("$.lastName", is(modelT.getLastName())))
                .andExpect(jsonPath("$.email", is(modelT.getEmail())))
                .andExpect(jsonPath("$.username", is(modelT.getUsername())));
    }

    @Test
    @WithMockUser(username = CORRECT_USER_USERNAME)
    public void getTrainerByUserIdAndTrainerIdShouldReturnBadRequest() throws Exception {
        Long trainerId = 2L;
        mockMvc.perform(get("/users/{userId}/trainers/{trainerId}", WRONG_USER_ID, trainerId))
                .andExpect(status().is(HttpStatus.BAD_REQUEST.value()));
    }

    /**
     * UserController - getUserRequestsByTrainersId(...) TESTS
     */

    @Test
    @WithMockUser(username = CORRECT_USER_USERNAME)
    public void getTrainerByUserIdAndTrainerIdShouldReturnCorrect2() throws Exception {
        RequestServiceModel modelT = new RequestServiceModel();
        modelT.setId(1L);
        modelT.setProfilePicture("RelationStatus.NotAnswered");
        modelT.setUsername("ädsads");

        Mockito.when(this.userTrainerService.getAllRequestsForUser(CORRECT_USER_ID))
                .thenReturn(List.of(modelT));

        mockMvc.perform(get("/users/{userId}/trainerRequests", CORRECT_USER_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].username", is(modelT.getUsername())))
                .andExpect(jsonPath("$[0].profilePicture", is(modelT.getProfilePicture())));
    }

    @Test
    @WithMockUser(username = CORRECT_USER_USERNAME)
    public void getUserRequestsByTrainersIdShouldReturnBadRequest() throws Exception {
        mockMvc.perform(get("/users/{userId}/trainerRequests", WRONG_USER_ID))
                .andExpect(status().is(HttpStatus.BAD_REQUEST.value()));
    }

    /**
     * UserController - checkIfUserNameExistsStartingWith(...) TESTS
     */

    @Test
    public void checkIfUserNameExistsStartingWithShouldReturnCorrect() throws Exception {
        String username = "demoName";
        Mockito.when(this.userService.checkIfUsernameExists(username))
                .thenReturn(true);

        mockMvc.perform(get("/users/checkUsername/{username}", username))
                .andExpect(status().isOk());
    }

    @Test
    public void checkIfUserNameExistsStartingWithShouldReturnWrong() throws Exception {
        String username = "demoName";
        Mockito.when(this.userService.checkIfUsernameExists(username))
                .thenReturn(true);

        mockMvc.perform(get("/users/checkUsername/{username}", "Wrong Username"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = CORRECT_USER_USERNAME)
    public void checkIfUserNameExistsStartingWithShouldReturnBadRequest() throws Exception {
        mockMvc.perform(get("/users/checkUsername/{username}", WRONG_USER_ID))
                .andExpect(status().is(HttpStatus.FORBIDDEN.value()));
    }

    /**
     * UserController - checkIfEmailExistsStartingWith(...) TESTS
     */

    @Test
    public void checkIfEmailExistsStartingWithShouldReturnCorrect() throws Exception {
        String email = "demoName";
        Mockito.when(this.userService.checkIfEmailExists(email))
                .thenReturn(true);

        mockMvc.perform(get("/users/checkEmail/{email}", email))
                .andExpect(status().isOk());
    }

    @Test
    public void checkIfEmailExistsStartingWithShouldReturnWrong() throws Exception {
        String email = "demoName";
        Mockito.when(this.userService.checkIfUsernameExists(email))
                .thenReturn(true);

        mockMvc.perform(get("/users/checkEmail/{email}", "Wrong Email"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = CORRECT_USER_USERNAME)
    public void checkIfEmailExistsStartingWithShouldReturnBadRequest() throws Exception {
        mockMvc.perform(get("/users/checkEmail/{email}", WRONG_USER_ID))
                .andExpect(status().is(HttpStatus.FORBIDDEN.value()));
    }

    /**
     * UserController - getAllWorkoutsForToday(...) TESTS
     */

    @Test
    @WithMockUser(username = CORRECT_USER_USERNAME)
    public void workoutServiceShouldReturnCorrect() throws Exception {
        TodayServiceModel todayServiceModel = new TodayServiceModel();
        todayServiceModel.setWorkoutWithLinks(234435);
        todayServiceModel.setWorkoutsExercisesCount(2434);
        todayServiceModel.setWorkoutsCount(234);
        todayServiceModel.setWorkouts(new ArrayList<>());
        Mockito.when(this.workoutService.getAllWorkoutsForToday(LocalDate.now(), CORRECT_USER_ID))
                .thenReturn(todayServiceModel);

        mockMvc.perform(get("/users/{userId}/today", CORRECT_USER_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.workouts", hasSize(0)))
                .andExpect(jsonPath("$.workoutsCount", is(todayServiceModel.getWorkoutsCount())))
                .andExpect(jsonPath("$.workoutsExercisesCount", is(todayServiceModel.getWorkoutsExercisesCount())))
                .andExpect(jsonPath("$.workoutWithLinks", is(todayServiceModel.getWorkoutWithLinks())));
    }

    @Test
    @WithMockUser(username = CORRECT_USER_USERNAME)
    public void workoutServiceShouldReturnBadRequest() throws Exception {
        mockMvc.perform(get("/users/checkEmail/{email}", WRONG_USER_ID))
                .andExpect(status().is(HttpStatus.FORBIDDEN.value()));
    }
}
