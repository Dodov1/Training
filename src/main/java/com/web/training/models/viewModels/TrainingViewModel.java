package com.web.training.models.viewModels;

import com.web.training.models.enums.DifficultyType;
import com.web.training.models.enums.StatusType;
import com.web.training.models.enums.TrainingType;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class TrainingViewModel {

    private Long id;
    private LocalDate fromDate;
    private LocalDate toDate;
    private String title;
    private StatusType statusType;
    private String description;
    private TrainingType trainingType;
    private DifficultyType difficulty;
}
