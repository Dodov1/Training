package com.web.training.models.bindingModels;

import com.web.training.models.enums.DifficultyType;
import com.web.training.models.enums.StatusType;
import com.web.training.models.enums.TrainingType;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Getter
@Setter
public class TrainingInfoEditBindingModel {

    @NotNull
    private Long id;
    @NotEmpty
    private String title;
    @NotNull
    private String description;
    @NotNull
    private LocalDate fromDate;
    @NotNull
    private LocalDate toDate;
    @NotNull
    private TrainingType trainingType;
    @NotNull
    private StatusType statusType;
    @NotNull
    private DifficultyType difficulty;
}
