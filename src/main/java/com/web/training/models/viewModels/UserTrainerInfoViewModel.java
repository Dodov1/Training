package com.web.training.models.viewModels;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserTrainerInfoViewModel {

    private Long id;
    private String username;
    private Integer trainingCount;
    private Integer age;
    private Integer height;
    private String profilePicture;

}
