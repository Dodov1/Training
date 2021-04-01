package com.web.training.models.entities;

import com.web.training.models.enums.WorkoutType;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "workouts")
public class Workout extends BaseEntity {

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "type", nullable = false)
    @Enumerated(EnumType.STRING)
    private WorkoutType type;

    @ManyToOne
    @NotNull
    private Day day;

    @OneToMany(mappedBy = "workout", cascade = CascadeType.REMOVE, fetch = FetchType.EAGER)
    private Set<Exercise> exercises;

    @Column(name = "link")
    private String link;
}
