package com.web.training.web;

import com.web.training.models.enums.DayStatus;
import com.web.training.models.serviceModels.DayFullInfoServiceModel;
import com.web.training.service.DayService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class DayControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DayService dayService;

    private DayFullInfoServiceModel model;

    private static final Long CORRECT_USER_ID = 1L;
    private static final Long CORRECT_TRAINING_ID = 43L;
    private static final Long CORRECT_DAY_ID = 12312L;
    private static final String CORRECT_USER_USERNAME = "demo";
    private static final String WRONG_USER_ID = "ASD";

    @BeforeEach
    public void init() {
        this.model = new DayFullInfoServiceModel();
        this.model.setId(1L);
        this.model.setDate(LocalDate.now());
        this.model.setStatus(DayStatus.InProgress);
    }

    /**
     * PictureController - getCurrentWeightForUserId(...) TESTS
     */

    @Test
    @WithMockUser(username = CORRECT_USER_USERNAME)
    public void getProfileImageForUserShouldReturnCorrectInfo() throws Exception {
        Mockito.when(this.dayService.getFullInfoById(CORRECT_USER_ID, CORRECT_TRAINING_ID, CORRECT_DAY_ID))
                .thenReturn(model);

        mockMvc.perform(get("/users/{userId}/trainings/{trainingId}/days/{dayId}/fullInfo",
                CORRECT_USER_ID, CORRECT_TRAINING_ID, CORRECT_DAY_ID))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.status", is(model.getStatus().toString())));
    }

    @Test
    @WithMockUser(username = CORRECT_USER_USERNAME)
    public void getProfileImageForUserShouldReturnBadRequest() throws Exception {
        mockMvc.perform(get("/users/{id}/pictures/profileImage", WRONG_USER_ID))
                .andExpect(status().is(HttpStatus.BAD_REQUEST.value()));
    }

}
