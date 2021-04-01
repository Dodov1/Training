package com.web.training.models.serviceModels;

import com.web.training.models.enums.TrainerType;

import java.time.LocalDate;

public class TrainerFullInfoServiceModel {

    private Long id;
    private String username;
    private String firstName;
    private String lastName;
    private TrainerType type;
    private String phoneNumber;
    private LocalDate fromDate;
    private Double rating;
    private Integer totalUsers;

    public Double getRating() {
        return this.rating;
    }

    public TrainerFullInfoServiceModel setRating(Double rating) {
        this.rating = rating;
        return this;
    }

    public Integer getTotalUsers() {
        return this.totalUsers;
    }

    public TrainerFullInfoServiceModel setTotalUsers(Integer totalUsers) {
        this.totalUsers = totalUsers;
        return this;
    }

    public String getPhoneNumber() {
        return this.phoneNumber;
    }

    public TrainerFullInfoServiceModel setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        return this;
    }

    public LocalDate getFromDate() {
        return this.fromDate;
    }

    public TrainerFullInfoServiceModel setFromDate(LocalDate fromDate) {
        this.fromDate = fromDate;
        return this;
    }

    public String getUsername() {
        return this.username;
    }

    public TrainerFullInfoServiceModel setUsername(String username) {
        this.username = username;
        return this;
    }

    public Long getId() {
        return this.id;
    }

    public TrainerFullInfoServiceModel setId(Long id) {
        this.id = id;
        return this;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public TrainerFullInfoServiceModel setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public String getLastName() {
        return this.lastName;
    }

    public TrainerFullInfoServiceModel setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public TrainerType getType() {
        return this.type;
    }

    public TrainerFullInfoServiceModel setType(TrainerType type) {
        this.type = type;
        return this;
    }
}
