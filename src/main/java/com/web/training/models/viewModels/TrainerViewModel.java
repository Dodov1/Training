package com.web.training.models.viewModels;

import com.web.training.models.enums.TrainerType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TrainerViewModel {

    private Long id;
    private String username;
    private String firstName;
    private String lastName;
    private TrainerType type;

}
