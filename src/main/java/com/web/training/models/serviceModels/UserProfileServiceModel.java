package com.web.training.models.serviceModels;

import com.web.training.models.enums.RelationStatus;

public class UserProfileServiceModel {

    private Long id;
    private String firstName;
    private String username;
    private String lastName;
    private String email;
    private Integer age;
    private Integer height;
    private RelationStatus statusType;

    public Long getId() {
        return this.id;
    }

    public UserProfileServiceModel setId(Long id) {
        this.id = id;
        return this;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public UserProfileServiceModel setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public String getUsername() {
        return this.username;
    }

    public UserProfileServiceModel setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getLastName() {
        return this.lastName;
    }

    public UserProfileServiceModel setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public String getEmail() {
        return this.email;
    }

    public UserProfileServiceModel setEmail(String email) {
        this.email = email;
        return this;
    }

    public Integer getAge() {
        return this.age;
    }

    public UserProfileServiceModel setAge(Integer age) {
        this.age = age;
        return this;
    }

    public Integer getHeight() {
        return this.height;
    }

    public UserProfileServiceModel setHeight(Integer height) {
        this.height = height;
        return this;
    }

    public RelationStatus getStatusType() {
        return this.statusType;
    }

    public UserProfileServiceModel setStatusType(RelationStatus statusType) {
        this.statusType = statusType;
        return this;
    }
}
