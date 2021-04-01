package com.web.training.service.impl;

import com.web.training.models.exceptionModels.NotFoundException;
import com.web.training.models.bindingModels.TrainingEditBindingModel;
import com.web.training.models.entities.Trainer;
import com.web.training.models.entities.TrainerTraining;
import com.web.training.models.entities.Training;
import com.web.training.models.enums.RelationStatus;
import com.web.training.models.serviceModels.*;
import com.web.training.repositories.TrainerRepository;
import com.web.training.repositories.TrainerTrainingRepository;
import com.web.training.repositories.TrainingRepository;
import com.web.training.service.TrainerTrainingService;
import com.web.training.service.TrainingService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.web.training.config.appConstants.AppConstants.NO_TRAINER_WITH_ID;
import static com.web.training.config.appConstants.AppConstants.NO_TRAINING_FOUND_WITH_ID;

@Service
public class TrainerTrainingServiceImpl implements TrainerTrainingService {

    private final TrainerTrainingRepository trainerTrainingRepository;
    private final TrainerRepository trainerRepository;
    private final TrainingRepository trainingRepository;
    private final TrainingService trainingService;
    private final ModelMapper modelMapper;

    public TrainerTrainingServiceImpl(TrainerTrainingRepository trainerTrainingRepository, TrainerRepository trainerRepository, TrainingRepository trainingRepository, TrainingService trainingService, ModelMapper modelMapper) {
        this.trainerTrainingRepository = trainerTrainingRepository;
        this.trainerRepository = trainerRepository;
        this.trainingRepository = trainingRepository;
        this.trainingService = trainingService;
        this.modelMapper = modelMapper;
    }

    @Override
    public void addNewTrainingForTrainer(Long trainingId, Long trainerId) {
        Training training = this.trainingRepository.findById(trainingId)
                .orElseThrow(() -> new NotFoundException(String.format(NO_TRAINING_FOUND_WITH_ID, trainingId)));
        Trainer trainer = this.trainerRepository.getByIdAndStatus(trainerId, RelationStatus.Accepted)
                .orElseThrow(() -> new NotFoundException(String.format(NO_TRAINER_WITH_ID, trainerId)));
        TrainerTraining trainerTraining = new TrainerTraining();
        trainerTraining.setTrainer(trainer);
        trainerTraining.setTraining(training);
        this.trainerTrainingRepository.saveAndFlush(trainerTraining);
    }

    @Override
    public List<TrainingBasicInfoServiceModel> getReadyTrainings(Long trainerId) {
        List<TrainerTraining> trainerTrainings = this.trainerTrainingRepository.getAllByTrainerIdAndTraining_UserIsNull(trainerId);
        return trainerTrainings
                .stream()
                .map(el -> this.modelMapper.map(el.getTraining(), TrainingBasicInfoServiceModel.class))
                .collect(Collectors.toList());
    }

    @Override
    public TrainingFullInfoServiceModel getReadyTrainingById(Long trainerId, Long trainingId) {
        return this.trainerTrainingRepository.getByTrainerIdAndTrainingId(trainerId, trainingId)
                .map(el -> this.modelMapper.map(el, TrainingFullInfoServiceModel.class)
                        .setDays(el.getTraining().getDays()
                                .stream()
                                .map(d -> this.modelMapper.map(d, DayFullInfoServiceModel.class)
                                        .setWorkoutsCount(d.getWorkouts().size()))
                                .collect(Collectors.toList()))
                ).orElseThrow(() -> new NotFoundException(String.format(NO_TRAINING_FOUND_WITH_ID, trainingId)));
    }

    @Override
    public ReadyTrainingServiceModel editByTrainerAndTrainingById(Long trainerId, Long trainingId, TrainingEditBindingModel trainingEditBindingModel) {
        return this.trainerTrainingRepository.getByTrainerIdAndTrainingIdAndTraining_UserIsNull(trainerId, trainingId)
                .map(el -> this.trainingService.editReadyTrainingById(trainingId, trainingEditBindingModel))
                .orElseThrow(() -> new NotFoundException(String.format(NO_TRAINING_FOUND_WITH_ID, trainingId)));
    }
}
