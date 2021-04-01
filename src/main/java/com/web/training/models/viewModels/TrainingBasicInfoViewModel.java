package com.web.training.models.viewModels;

import com.web.training.models.enums.DifficultyType;
import com.web.training.models.enums.TrainingType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TrainingBasicInfoViewModel {

    private Long id;
    public String title;
    public TrainingType trainingType;
    public DifficultyType difficulty;
}
