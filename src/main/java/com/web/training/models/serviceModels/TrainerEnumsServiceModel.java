package com.web.training.models.serviceModels;

import com.web.training.models.enums.TrainerType;

import java.util.List;

public class TrainerEnumsServiceModel {

    private List<TrainerType> trainerEnums;

    public List<TrainerType> getTrainerEnums() {
        return this.trainerEnums;
    }

    public TrainerEnumsServiceModel setTrainerEnums(List<TrainerType> trainerEnums) {
        this.trainerEnums = trainerEnums;
        return this;
    }
}
