package com.web.training.models.viewModels;

import com.web.training.models.enums.RelationStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserProfileViewModel {

    private Long id;
    private String firstName;
    private String username;
    private String lastName;
    private String password;
    private String email;
    private Integer age;
    private Integer height;
    private RelationStatus statusType;

}
