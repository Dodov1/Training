package com.web.training.web;

import com.web.training.models.bindingModels.WeightBindingModel;
import com.web.training.models.serviceModels.WeightServiceModel;
import com.web.training.models.serviceModels.WeightStatisticServiceModel;
import com.web.training.models.viewModels.WeightStatisticViewModel;
import com.web.training.models.viewModels.WeightViewModel;
import com.web.training.service.WeightService;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("**users/{userId}/weights")
public class WeightController {

    private final WeightService weightService;
    private final ModelMapper modelMapper;

    public WeightController(WeightService weightService, ModelMapper modelMapper) {
        this.weightService = weightService;
        this.modelMapper = modelMapper;
    }

    @PostMapping(value = "/add")
    public ResponseEntity<WeightViewModel> addNewWeight(@Valid @RequestBody WeightBindingModel weightDto, @PathVariable("userId") Long userId) {
        WeightServiceModel newWeight = this.weightService.addWeightToUser(userId, weightDto);
        return ResponseEntity.ok(this.modelMapper.map(newWeight, WeightViewModel.class));
    }

    @GetMapping("/current")
    public ResponseEntity<WeightViewModel> getCurrentWeightForUserId(@PathVariable("userId") Long userId) {
        WeightServiceModel weight = this.weightService.getCurrentWeightByUserId(userId);
        return ResponseEntity.ok(this.modelMapper.map(weight, WeightViewModel.class));
    }

    @GetMapping("/statistics")
    public ResponseEntity<WeightStatisticViewModel> getWeightStatisticsByUserId(@PathVariable("userId") Long userId) {
        WeightStatisticServiceModel byTrainingId = this.weightService.getWeightStatisticForUserId(userId);
        return ResponseEntity.ok(this.modelMapper.map(byTrainingId, WeightStatisticViewModel.class));
    }

}
