package com.web.training.service;

import com.web.training.models.serviceModels.PictureServiceModel;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface PictureService {

    PictureServiceModel uploadPicture(MultipartFile multipartFile, Long user) throws IOException;

    PictureServiceModel getPictureForUserId(Long user);

    PictureServiceModel getPictureForTrainerId(Long trainerId);
}
