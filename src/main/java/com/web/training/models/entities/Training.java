package com.web.training.models.entities;


import com.web.training.models.enums.DifficultyType;
import com.web.training.models.enums.StatusType;
import com.web.training.models.enums.TrainingType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "trainings")
public class Training extends BaseEntity {

    @Column(name = "from_date", nullable = false)
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private LocalDate fromDate;

    @Column(name = "to_date", nullable = false)
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private LocalDate toDate;

    @Enumerated(EnumType.STRING)
    @NotNull
    private TrainingType trainingType;

    @Column(name = "difficulty", nullable = false)
    @Enumerated(EnumType.STRING)
    private DifficultyType difficulty;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description", nullable = false)
    private String description;

    @OneToMany(mappedBy = "training", cascade = CascadeType.REMOVE, fetch = FetchType.EAGER)
    private Set<Day> days;

    @OneToMany(mappedBy = "training", cascade = CascadeType.ALL)
    private Set<TrainerTraining> trainer;

    @ManyToOne
    private User user;

    @NotNull
    @Enumerated(EnumType.STRING)
    private StatusType statusType;

}
