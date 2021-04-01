package com.web.training.models.viewModels;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class TrainingFullInfoViewModel {

    private TrainingViewModel training;
    private List<DayFullInfoViewModel> days;
}
