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
@Table(name = "pictures")
public class Picture extends BaseEntity {

    @Column(name = "type", nullable = false)
    private String type;

    @Column(name = "location", nullable = false)
    private String location;

    @Column(name = "name", nullable = false)
    private String name;

    @OneToOne(mappedBy = "picture")
    private User user;
}
