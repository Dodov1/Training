package com.web.training.web;

import com.web.training.models.serviceModels.TrainingServiceModel;
import com.web.training.models.serviceModels.TrainingsServiceModel;
import com.web.training.service.TrainingService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.hasKey;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class TrainingControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TrainingService trainingService;


    private static final Long CORRECT_USER_ID = 1L;
    private static final String CORRECT_USER_USERNAME = "demo";
    private static final String WRONG_USER_ID = "ASD";

    /**
     * TrainingController - getAllUserTrainingsById(...) TESTS
     */

    @Test
    @WithMockUser(username = CORRECT_USER_USERNAME)
    public void getAllUserTrainingsByIdShouldReturnCorrect() throws Exception {
        int page = 0;
        int size = 1;
        String sortBy = "title";
        String orderBy = "asc";
        Mockito.when(this.trainingService.getAllTrainingsForUserId(CORRECT_USER_ID, page, size, sortBy, orderBy))
                .thenReturn(new TrainingsServiceModel()
                        .setTrainings(List.of(new TrainingServiceModel())));

        mockMvc.perform(get("/users/{userId}/trainings/?page={page}&size={size}&sortBy={sortBy}&orderBy={orderBy}",
                CORRECT_USER_ID, page, size, sortBy, orderBy
        ))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.trainings", hasSize(1)))
                .andExpect(jsonPath("$._links", hasKey("self")))
                .andExpect(jsonPath("$._links", hasKey("sortByFromDateAsc")))
                .andExpect(jsonPath("$._links", hasKey("sortByFromDateDesc")))
                .andExpect(jsonPath("$._links", hasKey("sortByTitleAsc")))
                .andExpect(jsonPath("$._links", hasKey("sortByTitleDesc")))
                .andExpect(jsonPath("$._links", hasKey("sortByStatusTypeAsc")))
                .andExpect(jsonPath("$._links", hasKey("sortByStatusTypeDesc")));
    }

    @Test
    @WithMockUser(username = CORRECT_USER_USERNAME)
    public void getAllUserTrainingsByIdShouldReturnCorrectLinks() throws Exception {
        int page = 0;
        int size = 1;
        String sortBy = "title";
        String orderBy = "asc";
        Mockito.when(this.trainingService.getAllTrainingsForUserId(CORRECT_USER_ID, page, size, sortBy, orderBy))
                .thenReturn(new TrainingsServiceModel()
                        .setTrainings(List.of(new TrainingServiceModel(), new TrainingServiceModel())));


        mockMvc.perform(get("/users/{userId}/trainings/?page={page}&size={size}&sortBy={sortBy}&orderBy={orderBy}",
                CORRECT_USER_ID, page, size, sortBy, orderBy
        ))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.trainings", hasSize(2)))
                .andExpect(jsonPath("$._links", hasKey("self")))
                .andExpect(jsonPath("$._links", hasKey("next")))
                .andExpect(jsonPath("$._links", hasKey("sortByFromDateAsc")))
                .andExpect(jsonPath("$._links", hasKey("sortByFromDateDesc")))
                .andExpect(jsonPath("$._links", hasKey("sortByTitleAsc")))
                .andExpect(jsonPath("$._links", hasKey("sortByTitleDesc")))
                .andExpect(jsonPath("$._links", hasKey("sortByStatusTypeAsc")))
                .andExpect(jsonPath("$._links", hasKey("sortByStatusTypeDesc")));
    }

    @Test
    @WithMockUser(username = CORRECT_USER_USERNAME)
    public void getAllUserTrainingsByIdShouldReturnCorrectLinksPage2() throws Exception {
        int page = 2;
        int size = 1;
        String sortBy = "title";
        String orderBy = "asc";
        Mockito.when(this.trainingService.getAllTrainingsForUserId(CORRECT_USER_ID, page, size, sortBy, orderBy))
                .thenReturn(new TrainingsServiceModel()
                        .setTrainings(List.of(new TrainingServiceModel(), new TrainingServiceModel()))
                        .setTotalPages(2));


        mockMvc.perform(get("/users/{userId}/trainings/?page={page}&size={size}&sortBy={sortBy}&orderBy={orderBy}",
                CORRECT_USER_ID, page, size, sortBy, orderBy
        ))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.trainings", hasSize(2)))
                .andExpect(jsonPath("$._links", hasKey("self")))
                .andExpect(jsonPath("$._links", hasKey("first")))
                .andExpect(jsonPath("$._links", hasKey("last")))
                .andExpect(jsonPath("$._links", hasKey("prev")))
                .andExpect(jsonPath("$._links", hasKey("sortByFromDateAsc")))
                .andExpect(jsonPath("$._links", hasKey("sortByFromDateDesc")))
                .andExpect(jsonPath("$._links", hasKey("sortByTitleAsc")))
                .andExpect(jsonPath("$._links", hasKey("sortByTitleDesc")))
                .andExpect(jsonPath("$._links", hasKey("sortByStatusTypeAsc")))
                .andExpect(jsonPath("$._links", hasKey("sortByStatusTypeDesc")));
    }

    @Test
    @WithMockUser(username = CORRECT_USER_USERNAME)
    public void getAllUserTrainingsByIdShouldReturnCorrectLinksPage3() throws Exception {
        int page = 2;
        int size = 1;
        String sortBy = "title";
        String orderBy = "asc";
        Mockito.when(this.trainingService.getAllTrainingsForUserId(CORRECT_USER_ID, page, size, sortBy, orderBy))
                .thenReturn(new TrainingsServiceModel()
                        .setTrainings(List.of(new TrainingServiceModel(), new TrainingServiceModel())));


        mockMvc.perform(get("/users/{userId}/trainings/?page={page}&size={size}&sortBy={sortBy}&orderBy={orderBy}",
                CORRECT_USER_ID, page, size, sortBy, orderBy
        ))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.trainings", hasSize(2)))
                .andExpect(jsonPath("$._links", hasKey("self")))
                .andExpect(jsonPath("$._links", hasKey("first")))
                .andExpect(jsonPath("$._links", hasKey("next")))
                .andExpect(jsonPath("$._links", hasKey("last")))
                .andExpect(jsonPath("$._links", hasKey("prev")))
                .andExpect(jsonPath("$._links", hasKey("sortByFromDateAsc")))
                .andExpect(jsonPath("$._links", hasKey("sortByFromDateDesc")))
                .andExpect(jsonPath("$._links", hasKey("sortByTitleAsc")))
                .andExpect(jsonPath("$._links", hasKey("sortByTitleDesc")))
                .andExpect(jsonPath("$._links", hasKey("sortByStatusTypeAsc")))
                .andExpect(jsonPath("$._links", hasKey("sortByStatusTypeDesc")));
    }

    @Test
    @WithMockUser(username = CORRECT_USER_USERNAME)
    public void getAllUserTrainingsByIdShouldThrowBadRequest() throws Exception {
        int page = 0;
        int size = 0;
        String sortBy = "title";
        String orderBy = "asc";
        Mockito.when(this.trainingService.getAllTrainingsForUserId(CORRECT_USER_ID, page, size, sortBy, orderBy))
                .thenReturn(new TrainingsServiceModel());

        mockMvc.perform(get("/users/{userId}/trainings/?page={page}&size={size}&sortBy={sortBy}",
                CORRECT_USER_ID, page, size, sortBy
        ))
                .andExpect(status().is(HttpStatus.BAD_REQUEST.value()));
    }

    @Test
    @WithMockUser(username = CORRECT_USER_USERNAME)
    public void getAllUserTrainingsByIdShouldReturnBadRequest() throws Exception {
        mockMvc.perform(get("/users/{userId}/trainings", WRONG_USER_ID))
                .andExpect(status().is(HttpStatus.BAD_REQUEST.value()));
    }


}
