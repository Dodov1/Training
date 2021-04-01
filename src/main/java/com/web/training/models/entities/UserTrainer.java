package com.web.training.models.entities;

import com.web.training.models.enums.RelationStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users_trainers")
public class UserTrainer extends BaseEntity {

    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "trainer_id")
    private Trainer trainer;

    @Column(name = "is_trainer_requester", nullable = false)
    private Boolean isTrainerRequester;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private RelationStatus statusType;

    private LocalDate date;

    @Min(1)
    @Max(5)
    public Integer rating;
}
