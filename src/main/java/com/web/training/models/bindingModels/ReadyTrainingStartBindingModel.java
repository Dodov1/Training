package com.web.training.models.bindingModels;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Getter
@Setter
public class ReadyTrainingStartBindingModel {

    @NotNull
    private LocalDate startDate;
}
