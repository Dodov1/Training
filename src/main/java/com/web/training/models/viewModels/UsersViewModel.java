package com.web.training.models.viewModels;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UsersViewModel {

//    private List<EntityModel<TrainerUserInfoViewModel>> trainers;
    private List<TrainerUserInfoViewModel> trainers;
    private Integer totalPages;

}
