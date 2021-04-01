package com.web.training.models.viewModels;

import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.EntityModel;

import java.util.List;

@Getter
@Setter
public class TrainingWithLinksViewModel {

    private TrainingViewModel training;
    private List<EntityModel<DayIdViewModel>> days;
}
