package com.web.training.web;

import com.web.training.models.bindingModels.RespondTrainerRequestBindingModel;
import com.web.training.models.serviceModels.LoginLogServiceModel;
import com.web.training.models.serviceModels.TrainerServiceModel;
import com.web.training.models.viewModels.LoginLogViewModel;
import com.web.training.models.viewModels.TrainerViewModel;
import com.web.training.service.LogService;
import com.web.training.service.TrainerService;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/admins")
public class AdminController {

    private final TrainerService trainerService;
    private final LogService logService;
    private final ModelMapper modelMapper;

    public AdminController(TrainerService trainerService, LogService logService, ModelMapper modelMapper) {
        this.trainerService = trainerService;
        this.logService = logService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/trainerRequests")
    public ResponseEntity<List<TrainerViewModel>> getNotApprovedTrainers() {
        List<TrainerServiceModel> user = this.trainerService.getNotApprovedTrainers();
        return ResponseEntity.ok(user
                .stream()
                .map(el -> this.modelMapper.map(el, TrainerViewModel.class))
                .collect(Collectors.toList()));
    }

    @GetMapping("/log")
    public ResponseEntity<List<LoginLogViewModel>> getLoginLog() {
        List<LoginLogServiceModel> user = this.logService.getLog();
        return ResponseEntity.ok(user
                .stream()
                .map(el -> this.modelMapper.map(el, LoginLogViewModel.class))
                .collect(Collectors.toList())
        );
    }

    @PatchMapping("/{trainerId}/respondToRequest")
    public ResponseEntity<HttpStatus> respondToTrainerRequest(@Valid @RequestBody RespondTrainerRequestBindingModel requestDto, @PathVariable("trainerId") Long trainerId) {
        this.trainerService.respondToTrainerRequest(trainerId, requestDto);
        return ResponseEntity.ok().build();
    }
}
