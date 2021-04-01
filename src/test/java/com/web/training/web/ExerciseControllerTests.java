package com.web.training.web;

import com.google.gson.Gson;
import com.web.training.models.bindingModels.ChangeStatusBindingModel;
import com.web.training.models.enums.DurationType;
import com.web.training.models.enums.ExerciseStatusType;
import com.web.training.models.enums.ExerciseTarget;
import com.web.training.models.enums.ExerciseType;
import com.web.training.models.serviceModels.ExerciseServiceModel;
import com.web.training.service.ExerciseService;
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

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class ExerciseControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ExerciseService exerciseService;

    private ExerciseServiceModel model;

    private static final Long CORRECT_USER_ID = 1L;
    private static final Long CORRECT_WORKOUT_ID = 43L;
    private static final Long CORRECT_EXERCISE_ID = 12312L;
    private static final String CORRECT_USER_USERNAME = "demo";
    private static final String WRONG_USER_ID = "ASD";

    @BeforeEach
    public void init() {
        this.model = new ExerciseServiceModel();
        this.model.setId(1L);
        this.model.setStatus(ExerciseStatusType.Completed);
        this.model.setDuration(12);
        this.model.setDurationType(DurationType.min);
        this.model.setTarget(ExerciseTarget.Cadence);
        this.model.setType(ExerciseType.Interval);
    }

    /**
     * PictureController - getCurrentWeightForUserId(...) TESTS
     */

    @Test
    @WithMockUser(username = CORRECT_USER_USERNAME)
    public void getProfileImageForUserShouldReturnCorrectInfo() throws Exception {
        ChangeStatusBindingModel bindingModel = new ChangeStatusBindingModel();
        bindingModel.setStatusType(ExerciseStatusType.Completed);
        Mockito.when(this.exerciseService.setDoneByExerciseId
                (Mockito.any(ChangeStatusBindingModel.class), Mockito.eq(CORRECT_EXERCISE_ID), Mockito.eq(CORRECT_WORKOUT_ID), Mockito.eq(CORRECT_USER_ID)))
                .thenReturn(model);

        String toJson = new Gson().toJson(bindingModel);
        mockMvc.perform(patch(
                "/users/{userId}/workouts/{workoutId}/exercises/{exerciseId}",
                CORRECT_USER_ID, CORRECT_WORKOUT_ID, CORRECT_EXERCISE_ID
                )
                        .content(toJson)
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.status", is(model.getStatus().toString())))
                .andExpect(jsonPath("$.durationType", is(model.getDurationType().toString())))
                .andExpect(jsonPath("$.duration", is(model.getDuration())))
                .andExpect(jsonPath("$.target", is(model.getTarget().toString())));
    }

    @Test
    @WithMockUser(username = CORRECT_USER_USERNAME)
    public void getProfileImageForUserShouldReturnBadRequest() throws Exception {
        mockMvc.perform(patch("/users/{userId}/workouts/{workoutId}/exercises/{exerciseId}",
                WRONG_USER_ID, CORRECT_WORKOUT_ID, CORRECT_EXERCISE_ID)
        ).andExpect(status().is(HttpStatus.BAD_REQUEST.value()));
    }
}
