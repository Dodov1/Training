package com.web.training.models.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "logs")
public class Log extends BaseEntity {

    @Column(name = "message", nullable = false)
    private String message;


    @Column(name = "time", nullable = false)
    private LocalDateTime localDateTime;
}
