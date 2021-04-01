package com.web.training.models.serviceModels;

import com.web.training.models.enums.DurationType;
import com.web.training.models.enums.ExerciseStatusType;
import com.web.training.models.enums.ExerciseTarget;
import com.web.training.models.enums.ExerciseType;

public class ExerciseServiceModel {

    private Long id;
    private Integer duration;
    private ExerciseTarget target;
    private DurationType durationType;
    private ExerciseStatusType status;
    private ExerciseType type;

    public ExerciseTarget getTarget() {
        return this.target;
    }

    public ExerciseServiceModel setTarget(ExerciseTarget target) {
        this.target = target;
        return this;
    }

    public Long getId() {
        return this.id;
    }

    public ExerciseServiceModel setId(Long id) {
        this.id = id;
        return this;
    }

    public Integer getDuration() {
        return this.duration;
    }

    public ExerciseServiceModel setDuration(Integer duration) {
        this.duration = duration;
        return this;
    }

    public DurationType getDurationType() {
        return this.durationType;
    }

    public ExerciseServiceModel setDurationType(DurationType durationType) {
        this.durationType = durationType;
        return this;
    }

    public ExerciseStatusType getStatus() {
        return this.status;
    }

    public ExerciseServiceModel setStatus(ExerciseStatusType status) {
        this.status = status;
        return this;
    }

    public ExerciseType getType() {
        return this.type;
    }

    public ExerciseServiceModel setType(ExerciseType type) {
        this.type = type;
        return this;
    }
}
