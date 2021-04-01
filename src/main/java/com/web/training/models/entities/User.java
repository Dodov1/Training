package com.web.training.models.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User extends BaseEntity {

    @Column(name = "first_name", nullable = false)
    @Length(min = 2)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    @Length(min = 2)
    private String lastName;

    @Column(name = "username", nullable = false)
    @Length(min = 2)
    private String username;

    @Column(name = "password", nullable = false)
    @Length(min = 4)
    private String password;

    @Column(name = "email", nullable = false)
    @Pattern(regexp = "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9]))\\.){3}(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9])|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])")
    private String email;

    @Column(name = "age", nullable = false)
    @Min(1)
    private Integer age;

    @Column(name = "height", nullable = false)
    @Min(20)
    @Max(300)
    private Integer height;

    @Column(name = "enabled", nullable = false)
    private Boolean enabled;

    @OneToOne
    private Picture picture;

    @OneToOne(mappedBy = "user", fetch = FetchType.LAZY)
    private Trainer trainer;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private Set<UserTrainer> trainers;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private Set<Training> trainings;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private Set<Weight> weights;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Authority> authorities = new ArrayList<>();

    public User(String firstName, String lastName, String username, String password, String email, Integer age, Integer height, Boolean enabled, Trainer trainer, Picture picture) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.password = password;
        this.email = email;
        this.age = age;
        this.height = height;
        this.enabled = enabled;
        this.trainer = trainer;
        this.picture = picture;
        this.authorities = new ArrayList<>();
        this.weights = new HashSet<>();
        this.trainers = new HashSet<>();
        this.trainings = new HashSet<>();
    }
}
