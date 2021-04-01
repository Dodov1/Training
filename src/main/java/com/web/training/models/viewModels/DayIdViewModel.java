package com.web.training.models.viewModels;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class DayIdViewModel {

    private Long id;
    private LocalDate date;
    private Integer workoutsCount;
}
