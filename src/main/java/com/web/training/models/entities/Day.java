package com.web.training.models.entities;

import com.web.training.models.enums.DayStatus;
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
@Table(name = "days")
public class Day extends BaseEntity {

    @Column(name = "date", nullable = false)
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private LocalDate date;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    @NotNull
    private DayStatus status;

    @ManyToOne
    @NotNull
    private Training training;

    @OneToMany(mappedBy = "day", cascade = CascadeType.REMOVE, fetch = FetchType.EAGER)
    private Set<Workout> workouts;
}
