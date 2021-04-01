package com.web.training.models.viewModels;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class WeightViewModel {

    private Long id;
    private LocalDate date;
    private Double weight;
    private Double bmi;
    private String change;
}
