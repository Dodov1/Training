package com.web.training.models.viewModels;

import com.web.training.models.enums.RelationStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserViewModel {

    private Long id;
    private String firstName;
    private String username;
    private String lastName;
    private String email;
    private Integer age;
    private Integer height;
    private RelationStatus trainerStatus;
}
