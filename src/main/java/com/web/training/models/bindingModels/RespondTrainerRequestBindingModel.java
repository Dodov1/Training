package com.web.training.models.bindingModels;

import com.web.training.models.enums.RelationStatus;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class RespondTrainerRequestBindingModel {

    @NotNull
    private RelationStatus relationStatus;

}
