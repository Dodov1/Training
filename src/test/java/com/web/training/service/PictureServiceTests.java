package com.web.training.service;

import com.web.training.models.exceptionModels.NotFoundException;
import com.web.training.models.entities.Picture;
import com.web.training.models.entities.User;
import com.web.training.models.serviceModels.PictureServiceModel;
import com.web.training.repositories.PictureRepository;
import com.web.training.repositories.UserRepository;
import com.web.training.service.impl.PictureServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

import static com.web.training.config.appConstants.AppConstants.DEFAULT_PICTURE_NAME;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
public class PictureServiceTests {

    @Mock
    private UserRepository userRepository;
    @Mock
    private PictureRepository pictureRepository;
    private PictureService pictureService;

    private User testUser;
    private User testUser2;
    private Picture testPicture;
    private Picture testPicture2;

    private final ModelMapper modelMapper = new ModelMapper();

    private static final Long CORRECT_USER_ID = 1L;
    private static final Long CORRECT_PICTURE_ID = 1L;
    private static final Long WRONG_USER_ID = 2L;


    @BeforeEach
    public void init() {
        this.testUser = new User() {{
            setId(1L);
            setFirstName("PeshoF");
            setLastName("PeshoL");
            setUsername("PeshoU");
            setEmail("Pesho@mail.bg");
            setPicture(testPicture);
        }};
        this.testUser2 = new User() {{
            setId(2L);
            setFirstName("PeshoF2");
            setLastName("PeshoL2");
            setUsername("PeshoU2");
            setEmail("Pesho@mail2.bg");
        }};
        this.testPicture = new Picture() {{
            setUser(testUser2);
            setId(CORRECT_PICTURE_ID);
            setName(DEFAULT_PICTURE_NAME);
            setType(".png");
            setLocation("assets\\images\\profilePictures\\user2Pic.png");
        }};
        this.testPicture2 = new Picture() {{
            setUser(testUser);
            setId(CORRECT_PICTURE_ID);
            setName("user1Pic");
            setType(".jpeg");
            setLocation("assets\\images\\profilePictures\\user2Pic.png");
        }};
        this.pictureService = new PictureServiceImpl(this.pictureRepository, this.userRepository, this.modelMapper);
    }

    /**
     * PictureService - getPictureForUserId(...) TESTS
     */

    @Test
    void getPictureByUserIdShouldReturnCorrectId() {
        Mockito.when(this.pictureRepository
                .findByUserId(CORRECT_USER_ID))
                .thenReturn(Optional.of(this.testPicture));
        PictureServiceModel actual = this.pictureService.getPictureForUserId(CORRECT_USER_ID);
        Assertions.assertEquals(this.testPicture.getId(), actual.getId(), "Picture  is not correct");
    }

    @Test
    void getPictureByUserIdShouldReturnCorrectLocation() {
        Mockito.when(this.pictureRepository
                .findByUserId(CORRECT_USER_ID))
                .thenReturn(Optional.of(this.testPicture));
        PictureServiceModel actual = this.pictureService.getPictureForUserId(CORRECT_USER_ID);
        Assertions.assertEquals(this.testPicture.getLocation(), actual.getLocation(), "Picture  is not correct");
    }

    @Test
    void getPictureByWrongUserIdShouldThrowException() {
        assertThrows(NotFoundException.class, () -> this.pictureService.getPictureForUserId(WRONG_USER_ID));
    }

    /**
     * PictureService - getPictureForTrainerId(...) TESTS
     */

    @Test
    void getPictureByTrainerIdShouldReturnCorrectId() {
        Mockito.when(this.pictureRepository
                .getByUser_TrainerId(CORRECT_USER_ID))
                .thenReturn(Optional.of(this.testPicture2));
        PictureServiceModel actual = this.pictureService.getPictureForTrainerId(CORRECT_USER_ID);
        Assertions.assertEquals(this.testPicture2.getId(), actual.getId(), "Picture  is not correct");
    }

    @Test
    void getPictureByTrainerIdShouldReturnCorrectLocation() {
        Mockito.when(this.pictureRepository
                .getByUser_TrainerId(CORRECT_USER_ID))
                .thenReturn(Optional.of(this.testPicture2));
        PictureServiceModel actual = this.pictureService.getPictureForTrainerId(CORRECT_USER_ID);
        Assertions.assertEquals(this.testPicture2.getLocation(), actual.getLocation(), "Picture  is not correct");
    }

    @Test
    void getPictureByWrongTrainerIdShouldThrowException() {
        assertThrows(NotFoundException.class, () -> this.pictureService.getPictureForTrainerId(WRONG_USER_ID));
    }

    /**
     * PictureService - uploadPicture(...) TESTS
     */

    @Test
    void addPictureByUserIdShouldReturnCorrectLocation() throws IOException {
        byte[] bytes = new byte[2];
        bytes[0] = 1;
        bytes[1] = 2;
        MultipartFile multipartFile = new MockMultipartFile("demo", "imageTest", "type/jpeg", bytes);
        Mockito.when(this.pictureRepository
                .findByUserId(CORRECT_USER_ID))
                .thenReturn(Optional.of(this.testPicture2));
        Mockito.when(this.pictureRepository
                .saveAndFlush(this.testPicture2))
                .thenReturn(this.testPicture2);
        PictureServiceModel actual = this.pictureService.uploadPicture(multipartFile, CORRECT_USER_ID);
        Assertions.assertEquals(this.testPicture2.getLocation(), actual.getLocation(), "Picture  is not correct");
    }

    @Test
    void addPictureByUserIdShouldReturnCorrectId() throws IOException {
        byte[] bytes = new byte[2];
        bytes[0] = 1;
        bytes[1] = 2;
        MultipartFile multipartFile = new MockMultipartFile("demo", "imageTest", "type/jpeg", bytes);
        Mockito.when(this.pictureRepository
                .findByUserId(CORRECT_USER_ID))
                .thenReturn(Optional.of(this.testPicture2));
        Mockito.when(this.pictureRepository
                .saveAndFlush(this.testPicture2))
                .thenReturn(this.testPicture2);
        PictureServiceModel actual = this.pictureService.uploadPicture(multipartFile, CORRECT_USER_ID);
        Assertions.assertEquals(this.testPicture2.getId(), actual.getId(), "Picture  is not correct");
    }

    @Test
    void addPictureByUserIdWithDefaultPictureShouldReturnCorrectId() throws IOException {
        byte[] bytes = new byte[2];
        bytes[0] = 1;
        bytes[1] = 2;
        MultipartFile multipartFile = new MockMultipartFile("demo", "imageTest", "type/jpeg", bytes);
        Mockito.when(this.userRepository
                .findById(CORRECT_USER_ID))
                .thenReturn(Optional.of(this.testUser));
        Mockito.when(this.pictureRepository
                .findByUserId(CORRECT_USER_ID))
                .thenReturn(Optional.of(this.testPicture));
        Mockito.when(this.pictureRepository
                .saveAndFlush(any(Picture.class)))
                .thenReturn(this.testPicture2);
        PictureServiceModel actual = this.pictureService.uploadPicture(multipartFile, CORRECT_USER_ID);
        Assertions.assertEquals(this.testPicture2.getId(), actual.getId(), "Picture  is not correct");
    }

    @Test
    void addPictureByUserIdWithDefaultPictureShouldReturnCorrectLocation() throws IOException {
        byte[] bytes = new byte[2];
        bytes[0] = 1;
        bytes[1] = 2;
        MultipartFile multipartFile = new MockMultipartFile("demo", "imageTest", "type/jpeg", bytes);
        Mockito.when(this.userRepository
                .findById(CORRECT_USER_ID))
                .thenReturn(Optional.of(this.testUser));
        Mockito.when(this.pictureRepository
                .findByUserId(CORRECT_USER_ID))
                .thenReturn(Optional.of(this.testPicture));
        Mockito.when(this.pictureRepository
                .saveAndFlush(any(Picture.class)))
                .thenReturn(this.testPicture2);
        PictureServiceModel actual = this.pictureService.uploadPicture(multipartFile, CORRECT_USER_ID);
        Assertions.assertEquals(this.testPicture2.getLocation(), actual.getLocation(), "Picture  is not correct");
    }

    @Test
    void addPictureByWrongUserIdShouldThrowException() {
        byte[] bytes = new byte[2];
        bytes[0] = 1;
        bytes[1] = 2;
        MultipartFile multipartFile = new MockMultipartFile("demo", "imageTest", "type/jpeg", bytes);
        assertThrows(NotFoundException.class, () -> this.pictureService.uploadPicture(multipartFile, WRONG_USER_ID));
    }

    @Test
    void addPictureByUserIdShouldSetUserPicField() throws IOException {
        byte[] bytes = new byte[2];
        bytes[0] = 1;
        bytes[1] = 2;
        MultipartFile multipartFile = new MockMultipartFile("demo", "imageTest", "type/jpeg", bytes);
        Mockito.when(this.userRepository
                .findById(CORRECT_USER_ID))
                .thenReturn(Optional.of(this.testUser));
        Mockito.when(this.pictureRepository
                .findByUserId(CORRECT_USER_ID))
                .thenReturn(Optional.of(this.testPicture));
        Mockito.when(this.pictureRepository
                .saveAndFlush(any(Picture.class)))
                .thenReturn(this.testPicture2);
        this.pictureService.uploadPicture(multipartFile, CORRECT_USER_ID);
        Assertions.assertEquals(this.testPicture2, this.testUser.getPicture(), "Picture  is not correct");
    }
}
