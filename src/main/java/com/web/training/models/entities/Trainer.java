package com.web.training.models.entities;


import com.web.training.models.enums.RelationStatus;
import com.web.training.models.enums.TrainerType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "trainers")
@Getter
@Setter
public class Trainer extends BaseEntity {

    @OneToOne(fetch = FetchType.EAGER)
    @NotNull
    private User user;

    @Column(name = "type", nullable = false)
    @Enumerated(EnumType.STRING)
    @NotNull
    private TrainerType type;

    @OneToMany(mappedBy = "trainer", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    public Set<UserTrainer> users;

    @OneToMany(mappedBy = "trainer", cascade = CascadeType.ALL)
    public Set<TrainerTraining> trainings;

    @Enumerated(EnumType.STRING)
    private RelationStatus status;

    @Column(name = "from_Date", nullable = false)
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private LocalDate fromDate;

    @Column(name = "phone_number", nullable = false)
    @Pattern(regexp = "^\\s*(?:\\+?(\\d{1,3}))?[-. (]*(\\d{3})[-. )]*(\\d{3})[-. ]*(\\d{4})(?: *x(\\d+))?\\s*$")
    private String phoneNumber;

    public Trainer(User user, TrainerType type, RelationStatus status, LocalDate fromDate, String phoneNumber) {
        this.user = user;
        this.type = type;
        this.status = status;
        this.fromDate = fromDate;
        this.phoneNumber = phoneNumber;
        this.trainings = new HashSet<>();
        this.users = new HashSet<>();
    }
}
