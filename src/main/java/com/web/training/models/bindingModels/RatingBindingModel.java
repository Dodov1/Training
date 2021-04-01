package com.web.training.models.bindingModels;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class RatingBindingModel {

    @NotNull
    @Min(1)
    @Max(5)
    private Integer rating;
}
