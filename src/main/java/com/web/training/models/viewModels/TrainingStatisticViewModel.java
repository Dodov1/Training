package com.web.training.models.viewModels;


import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class TrainingStatisticViewModel {

    private List<String> labels;
    public List<Integer> data;
}
