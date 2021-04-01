package com.web.training.models.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "trainers_trainings")
public class TrainerTraining extends BaseEntity{

    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "training_id")
    private Training training;

    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "trainer_id")
    private Trainer trainer;
}
