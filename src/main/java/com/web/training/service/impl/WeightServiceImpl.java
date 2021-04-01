package com.web.training.service.impl;

import com.web.training.models.exceptionModels.NotFoundException;
import com.web.training.helpers.WeightCalculator;
import com.web.training.models.bindingModels.WeightBindingModel;
import com.web.training.models.entities.User;
import com.web.training.models.entities.Weight;
import com.web.training.models.serviceModels.WeightServiceModel;
import com.web.training.models.serviceModels.WeightStatisticServiceModel;
import com.web.training.repositories.UserRepository;
import com.web.training.repositories.WeightRepository;
import com.web.training.service.WeightService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.web.training.config.appConstants.AppConstants.*;

@Service
public class WeightServiceImpl implements WeightService {

    private final WeightRepository weightRepository;
    private final UserRepository userRepository;
    private final WeightCalculator weightCalculator;
    private final ModelMapper modelMapper;

    public WeightServiceImpl(WeightRepository weightRepository, UserRepository userRepository, WeightCalculator weightCalculator, ModelMapper modelMapper) {
        this.weightRepository = weightRepository;
        this.userRepository = userRepository;
        this.weightCalculator = weightCalculator;
        this.modelMapper = modelMapper;
    }

    @Override
    public WeightServiceModel addWeightToUser(Long ownerId, WeightBindingModel weightBindingModel) {
        User user = this.userRepository.findById(ownerId)
                .orElseThrow(() -> new NotFoundException(USER_NOT_FOUND_EXCEPTION_MESSAGE));
        Weight weight = this.modelMapper.map(weightBindingModel, Weight.class);
        weight.setUser(user);
        weight.setBmi(this.weightCalculator.calculateBmi(weightBindingModel.getWeight(), user.getHeight()));
        this.weightRepository.saveAndFlush(weight);
        return this.getCurrentWeightByUserId(ownerId);
    }

    @Override
    public WeightStatisticServiceModel getWeightStatisticForUserId(Long userId) {
        this.userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(USER_NOT_FOUND_EXCEPTION_MESSAGE));
        List<Weight> allByDateAfter = this.weightRepository.findAllByUserId(userId);
        WeightStatisticServiceModel statistic = new WeightStatisticServiceModel();
        if (allByDateAfter.isEmpty()) {
            return statistic;
        }
        Double min = this.weightRepository.getTop1ByUser_IdOrderByWeightAsc(userId).getWeight();
        Double max = this.weightRepository.getTop1ByUser_IdOrderByWeightDesc(userId).getWeight();
        allByDateAfter.forEach(el -> {
            statistic.getData().add(el.getWeight());
            statistic.getLabels().add(el.getDate().toLocalDate().toString());
        });
        statistic.setMaxWeight(max + WEIGHT_STATISTIC_VIEW_NUMBER);
        statistic.setMinWeight(min - WEIGHT_STATISTIC_VIEW_NUMBER);
        return statistic;
    }


    @Override
    public WeightServiceModel getCurrentWeightByUserId(Long userId) {
        User user = this.userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(USER_NOT_FOUND_EXCEPTION_MESSAGE));
        List<Weight> weights = this.weightRepository.getTop2ByUserIdOrderByDateDesc(userId);
        if (weights.isEmpty()) {
            throw new NotFoundException(NO_WEIGHT_DATA);
        }
        return this.initWeightServiceModel(weights, user);
    }

    private WeightServiceModel initWeightServiceModel(List<Weight> weights, User user) {
        WeightServiceModel weightServiceModel = this.modelMapper.map(weights.get(0), WeightServiceModel.class);
        weightServiceModel.setDate(weights.get(0).getDate().toLocalDate());
        weightServiceModel.setBmi(this.weightCalculator.calculateBmi(weightServiceModel.getWeight(), user.getHeight()));
        if (weights.size() == 1) {
            weightServiceModel.setChange(this.weightCalculator.getChange(weights.get(0).getWeight(), weights.get(0).getWeight()));
        } else if (weights.size() > 1) {
            weightServiceModel.setChange(this.weightCalculator.getChange(weights.get(0).getWeight(), weights.get(1).getWeight()));
        }
        return weightServiceModel;
    }
}
