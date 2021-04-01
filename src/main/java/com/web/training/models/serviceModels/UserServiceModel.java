package com.web.training.models.serviceModels;

import com.web.training.models.enums.RelationStatus;

public class UserServiceModel {

    private Long id;
    private String firstName;
    private String username;
    private String lastName;
    private String email;
    private Integer age;
    private Integer height;
    private RelationStatus trainerStatus;

    public RelationStatus getTrainerStatus() {
        return this.trainerStatus;
    }

    public UserServiceModel setTrainerStatus(RelationStatus trainerStatus) {
        this.trainerStatus = trainerStatus;
        return this;
    }

    public Long getId() {
        return this.id;
    }

    public UserServiceModel setId(Long id) {
        this.id = id;
        return this;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public UserServiceModel setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public String getUsername() {
        return this.username;
    }

    public UserServiceModel setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getLastName() {
        return this.lastName;
    }

    public UserServiceModel setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public String getEmail() {
        return this.email;
    }

    public UserServiceModel setEmail(String email) {
        this.email = email;
        return this;
    }

    public Integer getAge() {
        return this.age;
    }

    public UserServiceModel setAge(Integer age) {
        this.age = age;
        return this;
    }

    public Integer getHeight() {
        return this.height;
    }

    public UserServiceModel setHeight(Integer height) {
        this.height = height;
        return this;
    }
}
