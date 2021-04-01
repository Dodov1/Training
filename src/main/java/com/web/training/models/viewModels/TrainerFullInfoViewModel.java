package com.web.training.models.viewModels;

import com.web.training.models.enums.TrainerType;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class TrainerFullInfoViewModel {

    private Long id;
    private String username;
    private String firstName;
    private String lastName;
    private TrainerType type;
    private String phoneNumber;
    private LocalDate fromDate;
    private Double rating;
    private Integer totalUsers;
}
