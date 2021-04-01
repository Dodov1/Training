package com.web.training.models.entities;


import com.web.training.models.enums.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "exercises")
public class Exercise extends BaseEntity {

    @Column(name = "type", nullable = false)
    @Enumerated(EnumType.STRING)
    private ExerciseType type;

    @Column(name = "target", nullable = false)
    @Enumerated(EnumType.STRING)
    private ExerciseTarget target;

    @Column(name = "duration", nullable = false)
    @Min(1)
    private Integer duration;

    @Column(name = "duration_type", nullable = false)
    @Enumerated(EnumType.STRING)
    @NotNull
    private DurationType durationType;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    @NotNull
    private ExerciseStatusType status;

    @ManyToOne
    @NotNull
    private Workout workout;
}
