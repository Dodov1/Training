package com.web.training.models.viewModels;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class UserBasicPicViewModel {

    private Long id;
    private String firstName;
    private String username;
    private String lastName;
    private String profilePicture;
}
