package com.web.training.models.viewModels;

import com.web.training.models.serviceModels.TrainingServiceModel;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class TrainingsViewModel {

    private List<TrainingServiceModel> trainings;
    private Integer totalPages;

}
