package com.web.training.models.viewModels;

import com.web.training.models.enums.TrainerType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TrainerUserInfoViewModel {

    private Long id;
    private String username;
    private Integer usersCount;
    private TrainerType type;
    private Double rating;
    private String profilePicture;
}
