package com.web.training.models.bindingModels;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Setter
public class WeightBindingModel {

    @NotNull
    private LocalDateTime date;
    @Min(1)
    @NotNull
    private Double weight;
}
