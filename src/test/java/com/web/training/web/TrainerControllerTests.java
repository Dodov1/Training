package com.web.training.web;


import com.web.training.models.exceptionModels.UserNotFoundException;
import com.web.training.models.enums.DifficultyType;
import com.web.training.models.enums.RelationStatus;
import com.web.training.models.enums.TrainerType;
import com.web.training.models.enums.TrainingType;
import com.web.training.models.serviceModels.*;
import com.web.training.service.TrainerService;
import com.web.training.service.TrainerTrainingService;
import com.web.training.service.UserTrainerService;
import com.web.training.service.impl.UserServiceImpl;
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
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class TrainerControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TrainerService trainerService;
    @MockBean
    private TrainerTrainingService trainerTrainingService;
    @MockBean
    private UserTrainerService userTrainerService;
    @MockBean
    private UserServiceImpl userService;


    private static final Long CORRECT_TRAINER_ID = 1L;
    private static final Long CORRECT_USER_ID = 13L;
    private static final String CORRECT_USER_USERNAME = "demo";
    private static final String WRONG_USER_ID = "ASD";

    /**
     * TrainerController - getTrainerById(...) TESTS
     */

    @Test
    @WithMockUser(username = CORRECT_USER_USERNAME, roles = "TRAINER")
    public void getTrainerByIdShouldReturnCorrect() throws Exception {

        TrainerServiceModel model = new TrainerServiceModel();
        model.setId(1L);
        model.setFirstName("Fname");
        model.setLastName("Lname");
        model.setUsername("username");
        model.setType(TrainerType.Group);


        Mockito.when(this.trainerService.getTrainerById(CORRECT_TRAINER_ID))
                .thenReturn(model);

        mockMvc.perform(get("/trainers/{trainerId}", CORRECT_TRAINER_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.firstName", is(model.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(model.getLastName())))
                .andExpect(jsonPath("$.type", is(model.getType().toString())))
                .andExpect(jsonPath("$.username", is(model.getUsername())));
    }

    @Test
    @WithMockUser(username = CORRECT_USER_USERNAME)
    public void getTrainerByIdShouldReturnForbiddenRequest() throws Exception {
        mockMvc.perform(get("/trainers/{trainerId}", WRONG_USER_ID))
                .andExpect(status().is(HttpStatus.FORBIDDEN.value()));
    }

    @Test
    @WithMockUser(username = CORRECT_USER_USERNAME, roles = "TRAINER")
    public void getTrainerByIdShouldReturnBadRequest() throws Exception {
        mockMvc.perform(get("/trainers/{trainerId}", WRONG_USER_ID))
                .andExpect(status().is(HttpStatus.BAD_REQUEST.value()));
    }

    /**
     * TrainerController - getUserTrainersById(...) TESTS
     */

    @Test
    @WithMockUser(username = CORRECT_USER_USERNAME, roles = "TRAINER")
    public void getUserTrainersByIdShouldReturnCorrect() throws Exception {
        int page = 0;
        int size = 1;
        String sortBy = "id";
        String orderBy = "asc";
        Mockito.when(this.userTrainerService.getUsersPageForTrainerId(CORRECT_TRAINER_ID, page, size, sortBy, orderBy))
                .thenReturn(List.of(new UserTrainerInfoServiceModel()));

        mockMvc.perform(get("/trainers/{trainerId}/users/?page={page}&size={size}&sortBy={sortBy}&orderBy={orderBy}",
                CORRECT_TRAINER_ID, page, size, sortBy, orderBy
        ))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.users", hasSize(1)))
                .andExpect(jsonPath("$._links", hasKey("self")))
                .andExpect(jsonPath("$._links", hasKey("sortByAgeAsc")))
                .andExpect(jsonPath("$._links", hasKey("sortByAgeDesc")))
                .andExpect(jsonPath("$._links", hasKey("sortByHeightAsc")))
                .andExpect(jsonPath("$._links", hasKey("sortByHeightDesc")))
                .andExpect(jsonPath("$._links", hasKey("sortByTotalTrainingsAsc")))
                .andExpect(jsonPath("$._links", hasKey("sortByTotalTrainingsDesc")));
    }

    @Test
    @WithMockUser(username = CORRECT_USER_USERNAME, roles = "TRAINER")
    public void getUserTrainersByIdShouldReturnCorrectLinks() throws Exception {
        int page = 0;
        int size = 1;
        String sortBy = "id";
        String orderBy = "asc";
        Mockito.when(this.userTrainerService.getUsersPageForTrainerId(CORRECT_TRAINER_ID, page, size, sortBy, orderBy))
                .thenReturn(List.of(new UserTrainerInfoServiceModel(), new UserTrainerInfoServiceModel()));

        Mockito.when(this.userTrainerService.getTotalUserPagesCountForTrainerId(CORRECT_TRAINER_ID, size))
                .thenReturn(1);


        mockMvc.perform(get("/trainers/{trainerId}/users/?page={page}&size={size}&sortBy={sortBy}&orderBy={orderBy}",
                CORRECT_TRAINER_ID, page, size, sortBy, orderBy
        ))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.users", hasSize(2)))
                .andExpect(jsonPath("$._links", hasKey("self")))
                .andExpect(jsonPath("$._links", hasKey("sortByAgeAsc")))
                .andExpect(jsonPath("$._links", hasKey("sortByAgeDesc")))
                .andExpect(jsonPath("$._links", hasKey("sortByHeightAsc")))
                .andExpect(jsonPath("$._links", hasKey("sortByHeightDesc")))
                .andExpect(jsonPath("$._links", hasKey("sortByTotalTrainingsAsc")))
                .andExpect(jsonPath("$._links", hasKey("sortByTotalTrainingsDesc")));
    }

    @Test
    @WithMockUser(username = CORRECT_USER_USERNAME, roles = "TRAINER")
    public void getUserTrainersByIdShouldReturnCorrectLinksPage2() throws Exception {
        int page = 2;
        int size = 1;
        String sortBy = "id";
        String orderBy = "asc";
        Mockito.when(this.userTrainerService.getUsersPageForTrainerId(CORRECT_TRAINER_ID, page, size, sortBy, orderBy))
                .thenReturn(List.of(new UserTrainerInfoServiceModel(), new UserTrainerInfoServiceModel()));

        Mockito.when(this.userTrainerService.getTotalUserPagesCountForTrainerId(CORRECT_TRAINER_ID, size))
                .thenReturn(2);


        mockMvc.perform(get("/trainers/{trainerId}/users/?page={page}&size={size}&sortBy={sortBy}&orderBy={orderBy}",
                CORRECT_TRAINER_ID, page, size, sortBy, orderBy
        ))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.users", hasSize(2)))
                .andExpect(jsonPath("$._links", hasKey("self")))
                .andExpect(jsonPath("$._links", hasKey("first")))
                .andExpect(jsonPath("$._links", hasKey("last")))
                .andExpect(jsonPath("$._links", hasKey("prev")))
                .andExpect(jsonPath("$._links", hasKey("sortByAgeAsc")))
                .andExpect(jsonPath("$._links", hasKey("sortByAgeDesc")))
                .andExpect(jsonPath("$._links", hasKey("sortByHeightAsc")))
                .andExpect(jsonPath("$._links", hasKey("sortByHeightDesc")))
                .andExpect(jsonPath("$._links", hasKey("sortByTotalTrainingsAsc")))
                .andExpect(jsonPath("$._links", hasKey("sortByTotalTrainingsDesc")));
    }

    @Test
    @WithMockUser(username = CORRECT_USER_USERNAME, roles = "TRAINER")
    public void getUserTrainersByIdShouldReturnCorrectLinksPage3() throws Exception {
        int page = 2;
        int size = 1;
        String sortBy = "id";
        String orderBy = "asc";
        Mockito.when(this.userTrainerService.getUsersPageForTrainerId(CORRECT_TRAINER_ID, page, size, sortBy, orderBy))
                .thenReturn(List.of(new UserTrainerInfoServiceModel(), new UserTrainerInfoServiceModel()));

        Mockito.when(this.userTrainerService.getTotalUserPagesCountForTrainerId(CORRECT_TRAINER_ID, size))
                .thenReturn(3);


        mockMvc.perform(get("/trainers/{trainerId}/users/?page={page}&size={size}&sortBy={sortBy}&orderBy={orderBy}",
                CORRECT_TRAINER_ID, page, size, sortBy, orderBy
        ))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.users", hasSize(2)))
                .andExpect(jsonPath("$._links", hasKey("self")))
                .andExpect(jsonPath("$._links", hasKey("first")))
                .andExpect(jsonPath("$._links", hasKey("next")))
                .andExpect(jsonPath("$._links", hasKey("last")))
                .andExpect(jsonPath("$._links", hasKey("prev")))
                .andExpect(jsonPath("$._links", hasKey("sortByAgeAsc")))
                .andExpect(jsonPath("$._links", hasKey("sortByAgeDesc")))
                .andExpect(jsonPath("$._links", hasKey("sortByHeightAsc")))
                .andExpect(jsonPath("$._links", hasKey("sortByHeightDesc")))
                .andExpect(jsonPath("$._links", hasKey("sortByTotalTrainingsAsc")))
                .andExpect(jsonPath("$._links", hasKey("sortByTotalTrainingsDesc")));
    }

    @Test
    @WithMockUser(username = CORRECT_USER_USERNAME, roles = "TRAINER")
    public void getUserTrainersByIdShouldThrowBadRequest() throws Exception {
        int page = 0;
        int size = 0;
        String sortBy = "id";
        String orderBy = "asc";
        Mockito.when(this.userTrainerService.getUsersPageForTrainerId(CORRECT_TRAINER_ID, page, size, sortBy, orderBy))
                .thenReturn(List.of(new UserTrainerInfoServiceModel()));

        mockMvc.perform(get("/trainers/{trainerId}/users/?page={page}&size={size}&sortBy={sortBy}",
                CORRECT_TRAINER_ID, page, size, sortBy
        ))
                .andExpect(status().is(HttpStatus.BAD_REQUEST.value()));
    }

    @Test
    @WithMockUser(username = CORRECT_USER_USERNAME, roles = "TRAINER")
    public void getUserTrainersByIdShouldReturnBadRequest() throws Exception {
        mockMvc.perform(get("/trainers/{trainerId}/users", WRONG_USER_ID))
                .andExpect(status().is(HttpStatus.BAD_REQUEST.value()));
    }

    @Test
    @WithMockUser(username = CORRECT_USER_USERNAME)
    public void getUserTrainersByIdShouldReturnForbiddenRequest() throws Exception {
        mockMvc.perform(get("/trainers/{trainerId}", WRONG_USER_ID))
                .andExpect(status().is(HttpStatus.FORBIDDEN.value()));
    }

    /**
     * TrainerController - getUserByIdFromTrainerUsers(...) TESTS
     */

    @Test
    @WithMockUser(username = CORRECT_USER_USERNAME, roles = "TRAINER")
    public void getUserByIdFromTrainerUsersShouldReturnBadRequest() throws Exception {
        mockMvc.perform(get("/trainers/{trainerId}/users/{userId}", CORRECT_TRAINER_ID, WRONG_USER_ID))
                .andExpect(status().is(HttpStatus.BAD_REQUEST.value()));
    }

    @Test
    @WithMockUser(username = CORRECT_USER_USERNAME)
    public void getUserByIdFromTrainerUsersShouldReturnForbiddenRequest() throws Exception {
        mockMvc.perform(get("/trainers/{trainerId}/users/{userId}", CORRECT_TRAINER_ID, WRONG_USER_ID))
                .andExpect(status().is(HttpStatus.FORBIDDEN.value()));
    }

    @Test
    @WithMockUser(username = CORRECT_USER_USERNAME, roles = "TRAINER")
    public void getUserByIdFromTrainerUsersShouldReturnCorrect() throws Exception {
        UserProfileServiceModel modelT = new UserProfileServiceModel();
        modelT.setId(1L);
        modelT.setStatusType(RelationStatus.NotAnswered);
        modelT.setUsername("ädsads");
        modelT.setFirstName("1L");
        modelT.setLastName("1L");
        modelT.setAge(13);
        modelT.setHeight(163);
        Mockito.when(this.userTrainerService.getUserByIdForTrainer(CORRECT_TRAINER_ID, CORRECT_USER_ID))
                .thenReturn(modelT);

        mockMvc.perform(get("/trainers/{trainerId}/users/{userId}", CORRECT_TRAINER_ID, CORRECT_USER_ID))
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
    @WithMockUser(username = CORRECT_USER_USERNAME, roles = "TRAINER")
    public void getUserByIdFromTrainerUsersShouldReturnCorrectUnknownUser() throws Exception {
        UserServiceModel modelT = new UserServiceModel();
        modelT.setId(1L);
        modelT.setUsername("ädsads");
        modelT.setFirstName("1L");
        modelT.setLastName("1L");
        modelT.setAge(13);
        modelT.setHeight(163);
        Mockito.when(this.userTrainerService.getUserByIdForTrainer(CORRECT_TRAINER_ID, CORRECT_USER_ID))
                .thenThrow(new UserNotFoundException(""));

        Mockito.when(this.userService.getUserById(CORRECT_USER_ID))
                .thenReturn(modelT);
        mockMvc.perform(get("/trainers/{trainerId}/users/{userId}", CORRECT_TRAINER_ID, CORRECT_USER_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.age", is(modelT.getAge())))
                .andExpect(jsonPath("$.height", is(modelT.getHeight())))
                .andExpect(jsonPath("$.firstName", is(modelT.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(modelT.getLastName())))
                .andExpect(jsonPath("$.email", is(modelT.getEmail())))
                .andExpect(jsonPath("$.username", is(modelT.getUsername())));
    }

    /**
     * TrainerController - getUserRequestsByTrainersId(...) TESTS
     */

    @Test
    @WithMockUser(username = CORRECT_USER_USERNAME, roles = "TRAINER")
    public void getUserRequestsByTrainersIdShouldReturnBadRequest() throws Exception {
        mockMvc.perform(get("/trainers/{trainerId}/usersRequests", WRONG_USER_ID))
                .andExpect(status().is(HttpStatus.BAD_REQUEST.value()));
    }

    @Test
    @WithMockUser(username = CORRECT_USER_USERNAME)
    public void getUserRequestsByTrainersIdShouldReturnForbiddenRequest() throws Exception {
        mockMvc.perform(get("/trainers/{trainerId}/usersRequests", WRONG_USER_ID))
                .andExpect(status().is(HttpStatus.FORBIDDEN.value()));
    }

    @Test
    @WithMockUser(username = CORRECT_USER_USERNAME, roles = "TRAINER")
    public void getUserRequestsByTrainersIdShouldReturnCorrect2() throws Exception {
        RequestServiceModel modelT = new RequestServiceModel();
        modelT.setId(1L);
        modelT.setProfilePicture("RelationStatus.NotAnswered");
        modelT.setUsername("ädsads");

        Mockito.when(this.userTrainerService.getAllRequestsForTrainer(CORRECT_TRAINER_ID))
                .thenReturn(List.of(modelT));

        mockMvc.perform(get("/trainers/{trainerId}/usersRequests", CORRECT_TRAINER_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].username", is(modelT.getUsername())))
                .andExpect(jsonPath("$[0].profilePicture", is(modelT.getProfilePicture())));
    }

    /**
     * TrainerController - getTrainerReadyTrainingsTrainersId(...) TESTS
     */

    @Test
    @WithMockUser(username = CORRECT_USER_USERNAME, roles = "TRAINER")
    public void getTrainerReadyTrainingsTrainersIdShouldReturnBadRequest() throws Exception {
        mockMvc.perform(get("/trainers/{trainerId}/readyTrainings", WRONG_USER_ID))
                .andExpect(status().is(HttpStatus.BAD_REQUEST.value()));
    }

    @Test
    @WithMockUser(username = CORRECT_USER_USERNAME)
    public void getTrainerReadyTrainingsTrainersIdShouldReturnForbiddenRequest() throws Exception {
        mockMvc.perform(get("/trainers/{trainerId}/readyTrainings", WRONG_USER_ID))
                .andExpect(status().is(HttpStatus.FORBIDDEN.value()));
    }

    @Test
    @WithMockUser(username = CORRECT_USER_USERNAME, roles = "TRAINER")
    public void getUserRequestsByTrainersIdShouldReturnCorrect() throws Exception {
        TrainingBasicInfoServiceModel modelT = new TrainingBasicInfoServiceModel();
        modelT.setId(1L);
        modelT.setDifficulty(DifficultyType.Easy);
        modelT.setTitle("ASD");
        modelT.setTrainingType(TrainingType.Bike);

        Mockito.when(this.trainerTrainingService.getReadyTrainings(CORRECT_TRAINER_ID))
                .thenReturn(List.of(modelT));

        mockMvc.perform(get("/trainers/{trainerId}/readyTrainings", CORRECT_TRAINER_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].difficulty", is(modelT.getDifficulty().toString())))
                .andExpect(jsonPath("$[0].title", is(modelT.getTitle())))
                .andExpect(jsonPath("$[0].trainingType", is(modelT.getTrainingType().toString())));
    }

    /**
     * TrainerController - getTrainerReadyTrainingFullInfoTrainersId(...) TESTS
     */

    @Test
    @WithMockUser(username = CORRECT_USER_USERNAME, roles = "TRAINER")
    public void getTrainerReadyTrainingFullInfoTrainersIdShouldReturnBadRequest() throws Exception {
        mockMvc.perform(get("/trainers/{trainerId}/readyTrainings/{trainingId}", WRONG_USER_ID, 22L))
                .andExpect(status().is(HttpStatus.BAD_REQUEST.value()));
    }

    @Test
    @WithMockUser(username = CORRECT_USER_USERNAME)
    public void getTrainerReadyTrainingFullInfoTrainersIdShouldReturnForbiddenRequest() throws Exception {
        mockMvc.perform(get("/trainers/{trainerId}/readyTrainings/{trainingId}", WRONG_USER_ID, 22L))
                .andExpect(status().is(HttpStatus.FORBIDDEN.value()));
    }

    @Test
    @WithMockUser(username = CORRECT_USER_USERNAME, roles = "TRAINER")
    public void getTrainerReadyTrainingFullInfoTrainersIdShouldReturnCorrect() throws Exception {
        Long trainingId = 55L;
        TrainingFullInfoServiceModel modelT = new TrainingFullInfoServiceModel();
        TrainingServiceModel modelS = new TrainingServiceModel();
        modelS.setId(1L);
        modelS.setDifficulty(DifficultyType.Easy);
        modelS.setTitle("ASD");
        modelS.setTrainingType(TrainingType.Bike);
        modelT.setTraining(modelS);

        Mockito.when(this.trainerTrainingService.getReadyTrainingById(CORRECT_TRAINER_ID, trainingId))
                .thenReturn(modelT);

        mockMvc.perform(get("/trainers/{trainerId}/readyTrainings/{trainingId}", CORRECT_TRAINER_ID, trainingId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.training.id", is(1)))
                .andExpect(jsonPath("$.training.difficulty", is(modelT.getTraining().getDifficulty().toString())))
                .andExpect(jsonPath("$.training.title", is(modelT.getTraining().getTitle())))
                .andExpect(jsonPath("$.training.trainingType", is(modelT.getTraining().getTrainingType().toString())));
    }

    /**
     * TrainerController - getTrainerInfoByUsername(...) TESTS
     */

    @Test
    @WithMockUser(username = CORRECT_USER_USERNAME)
    public void getTrainerInfoByUsernameShouldReturnCorrect() throws Exception {
        TrainerFullInfoServiceModel modelT = new TrainerFullInfoServiceModel();
        modelT.setRating(23.5);
        modelT.setUsername("23.5");
        modelT.setId(23L);
        modelT.setFirstName("23.5FNAME");
        modelT.setLastName("23.5LNAME");
        modelT.setPhoneNumber("+368772625218");
        modelT.setFromDate(LocalDate.now());

        Mockito.when(this.trainerService.getTrainerInfoByUsername("CORRECT_TRAINER_ID"))
                .thenReturn(modelT);

        mockMvc.perform(get("/trainers/username/{username}", "CORRECT_TRAINER_ID"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(23)))
                .andExpect(jsonPath("$.phoneNumber", is(modelT.getPhoneNumber())))
                .andExpect(jsonPath("$.fromDate", is(modelT.getFromDate().toString())))
                .andExpect(jsonPath("$.firstName", is(modelT.getFirstName())))
                .andExpect(jsonPath("$.rating", is(modelT.getRating())))
                .andExpect(jsonPath("$.totalUsers", is(modelT.getTotalUsers())))
                .andExpect(jsonPath("$.lastName", is(modelT.getLastName())))
                .andExpect(jsonPath("$.username", is(modelT.getUsername())));
    }
}
