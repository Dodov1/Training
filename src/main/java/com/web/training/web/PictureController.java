package com.web.training.web;

import com.web.training.models.serviceModels.PictureServiceModel;
import com.web.training.models.viewModels.PictureViewModel;
import com.web.training.service.PictureService;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class PictureController {

    private final PictureService pictureService;
    private final ModelMapper modelMapper;

    public PictureController(PictureService pictureService, ModelMapper modelMapper) {
        this.pictureService = pictureService;
        this.modelMapper = modelMapper;
    }

    @PostMapping("**users/{userId}/pictures/upload")
    public ResponseEntity<PictureViewModel> uploadImage(@RequestParam("imageFile") MultipartFile file, @PathVariable("userId") Long userId) throws IOException {
        PictureServiceModel model = this.pictureService.uploadPicture(file, userId);
        return ResponseEntity.ok(this.modelMapper.map(model, PictureViewModel.class));
    }

    @GetMapping("**users/{userId}/pictures/profileImage")
    public ResponseEntity<PictureViewModel> getProfileImageForUser(@PathVariable("userId") Long userId) {
        PictureServiceModel result = this.pictureService.getPictureForUserId(userId);
        return ResponseEntity.ok(this.modelMapper.map(result, PictureViewModel.class));
    }

    @GetMapping("**users/{trainerId}/trainerProfileImage")
    public ResponseEntity<PictureViewModel> getProfileImageForTrainer(@PathVariable("trainerId") Long trainerId) {
        PictureServiceModel result = this.pictureService.getPictureForTrainerId(trainerId);
        return ResponseEntity.ok(this.modelMapper.map(result, PictureViewModel.class));
    }
}
