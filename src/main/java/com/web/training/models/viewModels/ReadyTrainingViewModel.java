package com.web.training.models.viewModels;

import com.web.training.models.enums.DifficultyType;
import com.web.training.models.enums.StatusType;
import com.web.training.models.enums.TrainingType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReadyTrainingViewModel {

    private Long id;
    private String title;
    private StatusType statusType;
    private String description;
    private TrainingType trainingType;
    private DifficultyType difficulty;
}
