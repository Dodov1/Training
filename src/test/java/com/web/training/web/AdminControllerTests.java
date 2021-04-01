package com.web.training.web;


import com.web.training.models.enums.TrainerType;
import com.web.training.models.serviceModels.LoginLogServiceModel;
import com.web.training.models.serviceModels.TrainerServiceModel;
import com.web.training.service.LogService;
import com.web.training.service.TrainerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class AdminControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TrainerService trainerService;
    @MockBean
    private LogService logService;

    private TrainerServiceModel model;
    private LoginLogServiceModel modelLog;

    private static final Long CORRECT_USER_ID = 1L;
    private static final String CORRECT_USER_USERNAME = "demo";
    private static final String WRONG_USER_ID = "ASD";

    @BeforeEach
    public void init() {
        this.model = new TrainerServiceModel();
        this.model.setId(1L);
        this.model.setFirstName("boobo");
        this.model.setLastName("demo");
        this.model.setType(TrainerType.Group);
        this.model.setUsername("asd");
        this.modelLog = new LoginLogServiceModel();
        this.modelLog.setMessage("asdfds");
        this.modelLog.setTime(LocalDateTime.MAX);
    }

    /**
     * AdminController - getNotApprovedTrainers(...) TESTS
     */

    @Test
    @WithMockUser(username = CORRECT_USER_USERNAME, roles = {"ADMIN"})
    public void getNotApprovedTrainersShouldReturnCorrectValues() throws Exception {
        Mockito.when(this.trainerService.getNotApprovedTrainers())
                .thenReturn(List.of(model));

        ResultActions perform = mockMvc.perform(get("/admins/trainerRequests"));

        perform
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].firstName", is(model.getFirstName())))
                .andExpect(jsonPath("$[0].lastName", is(model.getLastName())))
                .andExpect(jsonPath("$[0].username", is(model.getUsername())))
                .andExpect(jsonPath("$[0].type", is(model.getType().toString())));
    }

    @Test
    @WithMockUser(username = CORRECT_USER_USERNAME)
    public void getCurrentWeightForUserIdShouldThrowForbidden() throws Exception {
        Mockito.when(this.trainerService.getNotApprovedTrainers())
                .thenReturn(List.of(model));

        ResultActions perform = mockMvc.perform(get("/admins/trainerRequests"));
        perform
                .andExpect(status().isForbidden());
    }

    /**
     * AdminController - getLoginLog(...) TESTS
     */

    @Test
    @WithMockUser(username = CORRECT_USER_USERNAME, roles = {"ADMIN"})
    public void getLoginLogShouldReturnCorrectInfo() throws Exception {
        Mockito.when(this.logService.getLog())
                .thenReturn(List.of(modelLog));

        ResultActions perform = mockMvc.perform(get("/admins/log"));

        perform
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$[0].message", is(modelLog.getMessage())))
                .andExpect(jsonPath("$[0].time", is(modelLog.getTime().toString())));
    }

    @Test
    @WithMockUser(username = CORRECT_USER_USERNAME)
    public void getLoginLogShouldThrowForbidden() throws Exception {
        Mockito.when(this.trainerService.getNotApprovedTrainers())
                .thenReturn(List.of(model));

        ResultActions perform = mockMvc.perform(get("/admins/log"));
        perform
                .andExpect(status().isForbidden());
    }
}
