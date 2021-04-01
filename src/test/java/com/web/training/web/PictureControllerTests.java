package com.web.training.web;

import com.web.training.models.serviceModels.PictureServiceModel;
import com.web.training.service.PictureService;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class PictureControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PictureService pictureService;

    private PictureServiceModel model;

    private static final Long CORRECT_USER_ID = 1L;
    private static final String CORRECT_USER_USERNAME = "demo";
    private static final String WRONG_USER_ID = "ASD";

    @BeforeEach
    public void init() {
        this.model = new PictureServiceModel();
        this.model.setId(1L);
        this.model.setLocation("demo\\location");
    }

    /**
     * PictureController - getCurrentWeightForUserId(...) TESTS
     */

    @Test
    @WithMockUser(username = CORRECT_USER_USERNAME)
    public void getProfileImageForUserShouldReturnCorrectInfo() throws Exception {
        Mockito.when(this.pictureService.getPictureForUserId(CORRECT_USER_ID))
                .thenReturn(model);

        mockMvc.perform(get("/users/{id}/pictures/profileImage", CORRECT_USER_ID))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.location", is(model.getLocation())));
    }

    @Test
    @WithMockUser(username = CORRECT_USER_USERNAME)
    public void getProfileImageForUserShouldReturnBadRequest() throws Exception {
        mockMvc.perform(get("/users/{id}/pictures/profileImage", WRONG_USER_ID))
                .andExpect(status().is(HttpStatus.BAD_REQUEST.value()));
    }

    /**
     * PictureController - getCurrentWeightForUserId(...) TESTS
     */

    @Test
    @WithMockUser(username = CORRECT_USER_USERNAME)
    public void getProfileImageForTrainerShouldReturnCorrectInfo() throws Exception {
        Mockito.when(this.pictureService.getPictureForTrainerId(CORRECT_USER_ID))
                .thenReturn(model);

        mockMvc.perform(get("/users/{id}/trainerProfileImage", CORRECT_USER_ID))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.location", is(model.getLocation())));
    }

    @Test
    @WithMockUser(username = CORRECT_USER_USERNAME)
    public void getProfileImageForTrainerShouldReturnBadRequest() throws Exception {
        mockMvc.perform(get("/users/{id}/trainerProfileImage", WRONG_USER_ID))
                .andExpect(status().is(HttpStatus.BAD_REQUEST.value()));
    }

    /**
     * PictureController - uploadImage(...) TESTS
     */

//    @Test
//    @WithMockUser(username = CORRECT_USER_USERNAME)
//    public void uploadImageShouldReturnBadRequest() throws Exception {
//        mockMvc.perform(post("/users/{id}/pictures/upload", WRONG_USER_ID))
//                .andExpect(status().is(HttpStatus.BAD_REQUEST.value()));
//    }
}
