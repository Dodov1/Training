package com.web.training.web;

import com.web.training.models.serviceModels.WeightServiceModel;
import com.web.training.models.serviceModels.WeightStatisticServiceModel;
import com.web.training.service.WeightService;
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

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class WeightControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private WeightService weightService;

    private WeightServiceModel model;

    private static final Long CORRECT_USER_ID = 1L;
    private static final String CORRECT_USER_USERNAME = "demo";
    private static final String WRONG_USER_ID = "ASD";

    @BeforeEach
    public void init() {
        this.model = new WeightServiceModel();
        this.model.setId(1L);
        this.model.setWeight(15.7);
        this.model.setChange("--");
        this.model.setDate(LocalDate.now());
    }

    /**
     * WeightController - getCurrentWeightForUserId(...) TESTS
     */

    @Test
    @WithMockUser(username = CORRECT_USER_USERNAME)
    public void getCurrentWeightForUserIdShouldReturnCorrectInfo() throws Exception {
        Mockito.when(this.weightService.getCurrentWeightByUserId(CORRECT_USER_ID))
                .thenReturn(model);

        mockMvc.perform(get("/users/{id}/weights/current", CORRECT_USER_ID))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.bmi", is(model.getBmi())))
                .andExpect(jsonPath("$.date", is(model.getDate().toString())))
                .andExpect(jsonPath("$.change", is(model.getChange())));
    }

    @Test
    @WithMockUser(username = CORRECT_USER_USERNAME)
    public void getCurrentWeightForUserIdShouldReturnBadRequest() throws Exception {
        mockMvc.perform(get("/users/{id}/weights/current", WRONG_USER_ID))
                .andExpect(status().is(HttpStatus.BAD_REQUEST.value()));
    }

    /**
     * WeightController - getWeightStatisticsByUserId(...) TESTS
     */

    @Test
    @WithMockUser(username = CORRECT_USER_USERNAME)
    public void getWeightStatisticsByUserIdShouldReturnCorrectInfo() throws Exception {
        WeightStatisticServiceModel model = new WeightStatisticServiceModel();
        model.getData().add(1.0);
        model.getData().add(1.0);
        model.getLabels().add("1.0");
        model.setMinWeight(-10.0);
        model.setMaxWeight(10.0);
        Mockito.when(this.weightService.getWeightStatisticForUserId(CORRECT_USER_ID))
                .thenReturn(model);

        mockMvc.perform(get("/users/{id}/weights/statistics", CORRECT_USER_ID))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.minWeight", is(model.getMinWeight())))
                .andExpect(jsonPath("$.maxWeight", is(model.getMaxWeight())))
                .andExpect(jsonPath("$.labels", hasSize(model.getLabels().size())))
                .andExpect(jsonPath("$.data", hasSize(model.getData().size())));
    }

    @Test
    @WithMockUser(username = CORRECT_USER_USERNAME)
    public void getWeightStatisticsByUserIdShouldReturnBadRequest() throws Exception {
        mockMvc.perform(get("/users/{id}/weights/statistics", WRONG_USER_ID))
                .andExpect(status().is(HttpStatus.BAD_REQUEST.value()));
    }

    /**
     * WeightController - addNewWeight(...) TESTS
     */

}
