package com.web.training.web;

import com.web.training.models.serviceModels.DayFullInfoServiceModel;
import com.web.training.models.viewModels.DayFullInfoViewModel;
import com.web.training.service.DayService;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/**/users/{userId}/trainings/{trainingId}/days")
public class DayController {

    private final DayService dayService;
    private final ModelMapper modelMapper;

    public DayController(DayService dayService, ModelMapper modelMapper) {
        this.dayService = dayService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/{dayId}/fullInfo")
    public ResponseEntity<DayFullInfoViewModel> getFullDayById(@PathVariable("userId") Long userId,
                                                               @PathVariable("trainingId") Long trainingId, @PathVariable("dayId") Long dayId) {
        DayFullInfoServiceModel dayFullInfoById = this.dayService.getFullInfoById(userId, trainingId, dayId);
        return ResponseEntity.ok(this.modelMapper.map(dayFullInfoById, DayFullInfoViewModel.class));
    }

    @Scheduled(cron = "0 0 0 * * *", zone = "EET")
    public void markDaysAsFinished() {
        this.dayService.changeDayStatusScheduled();
    }
}
