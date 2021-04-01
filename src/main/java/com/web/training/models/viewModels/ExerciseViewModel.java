package com.web.training.models.viewModels;

import com.web.training.models.enums.DurationType;
import com.web.training.models.enums.ExerciseStatusType;
import com.web.training.models.enums.ExerciseTarget;
import com.web.training.models.enums.ExerciseType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExerciseViewModel {

    private Long id;
    private Integer duration;
    private DurationType durationType;
    private ExerciseStatusType status;
    private ExerciseTarget target;
    private ExerciseType type;
}
