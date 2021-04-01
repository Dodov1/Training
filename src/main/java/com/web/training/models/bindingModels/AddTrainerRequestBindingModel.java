package com.web.training.models.bindingModels;

import com.web.training.models.enums.TrainerType;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Getter
@Setter
public class AddTrainerRequestBindingModel {

    @NotNull
    private TrainerType trainerType;
    @NotEmpty
    @Pattern(regexp = "^\\s*(?:\\+?(\\d{1,3}))?[-. (]*(\\d{3})[-. )]*(\\d{3})[-. ]*(\\d{4})(?: *x(\\d+))?\\s*$")
    private String phoneNumber;
}
