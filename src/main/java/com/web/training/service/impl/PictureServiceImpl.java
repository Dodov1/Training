package com.web.training.service.impl;

import com.web.training.models.exceptionModels.NotFoundException;
import com.web.training.models.entities.Picture;
import com.web.training.models.entities.User;
import com.web.training.models.serviceModels.PictureServiceModel;
import com.web.training.repositories.PictureRepository;
import com.web.training.repositories.UserRepository;
import com.web.training.service.PictureService;
import lombok.SneakyThrows;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.Optional;

import static com.web.training.config.appConstants.AppConstants.*;

@Service
public class PictureServiceImpl implements PictureService {

    private final PictureRepository pictureRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    public PictureServiceImpl(PictureRepository pictureRepository, UserRepository userRepository, ModelMapper modelMapper) {
        this.pictureRepository = pictureRepository;
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public PictureServiceModel uploadPicture(MultipartFile multipartFile, Long userId) throws IOException {
        String imageType = Objects.requireNonNull(multipartFile.getContentType()).split("/")[1];
        String destination = System.getProperty("user.dir") + "\\my-app\\src\\assets\\images\\profilePictures\\" + String.format(PICTURE_NAMING, userId, imageType);
        String location = "assets\\images\\profilePictures\\" + String.format(PICTURE_NAMING, userId, imageType);
        File file = new File(destination);
        Optional<Picture> pictureByUserId = this.pictureRepository.findByUserId(userId);
        return pictureByUserId
                .map(e -> {
                    if (e.getName().equals(DEFAULT_PICTURE_NAME)) {
                        User user = userRepository.findById(userId)
                                .orElseThrow(() -> new NotFoundException(USER_NOT_FOUND_EXCEPTION_MESSAGE));
                        Picture picture = new Picture(multipartFile.getContentType(), location, String.format(PICTURE_NAMING, userId, imageType), user);
                        picture = this.pictureRepository.saveAndFlush(picture);
                        user.setPicture(picture);
                        this.userRepository.saveAndFlush(user);
                        this.transferFile(multipartFile, file);
                        return this.modelMapper.map(picture, PictureServiceModel.class);
                    } else {
                        e.setLocation(location);
                        e.setType(multipartFile.getContentType());
                        e.setName(String.format(PICTURE_NAMING, userId, imageType));
                        this.transferFile(multipartFile, file);
                        e = this.pictureRepository.saveAndFlush(e);
                        return this.modelMapper.map(e, PictureServiceModel.class);
                    }
                }).orElseThrow(() -> new NotFoundException(PICTURE_NOT_FOUND));
    }

    @SneakyThrows
    private void transferFile(MultipartFile multipartFile, File file) {
        multipartFile.transferTo(file);
    }

    @Override
    public PictureServiceModel getPictureForUserId(Long user) {
        return this.pictureRepository.findByUserId(user)
                .map(el -> this.modelMapper.map(el, PictureServiceModel.class))
                .orElseThrow(() -> new NotFoundException(String.format(NO_USER_WITH_ID, user)));
    }

    @Override
    public PictureServiceModel getPictureForTrainerId(Long trainerId) {
        return this.pictureRepository.getByUser_TrainerId(trainerId)
                .map(el -> this.modelMapper.map(el, PictureServiceModel.class))
                .orElseThrow(() -> new NotFoundException(String.format(NO_TRAINER_WITH_ID, trainerId)));
    }
}
