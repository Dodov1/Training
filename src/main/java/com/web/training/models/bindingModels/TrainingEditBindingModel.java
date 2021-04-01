package com.web.training.models.bindingModels;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
public class TrainingEditBindingModel {

    @NotNull
    private TrainingInfoEditBindingModel training;
    @NotNull
    private List<DayEditBindingModel> days;
}
