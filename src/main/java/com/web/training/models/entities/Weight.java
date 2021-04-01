package com.web.training.models.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "weights")
public class Weight extends BaseEntity {

    @Column(name = "date", nullable = false)
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private LocalDateTime date;

    @Column(name = "weight", nullable = false)
    @Min(1)
    private Double weight;

    @Column(name = "bmi", nullable = false)
    private Double bmi;

    @ManyToOne
    private User user;
}
