package com.web.training.models.viewModels;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class WeightStatisticViewModel {

    private List<String> labels;
    private List<Double> data;
    private Double maxWeight;
    private Double minWeight;
}
