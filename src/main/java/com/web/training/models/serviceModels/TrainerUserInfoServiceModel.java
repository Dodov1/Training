package com.web.training.models.serviceModels;

import com.web.training.models.enums.TrainerType;

public class TrainerUserInfoServiceModel {

    private Long id;
    private String username;
    private Integer usersCount;
    private TrainerType type;
    private Double rating;
    private String profilePicture;

    public Long getId() {
        return this.id;
    }

    public TrainerUserInfoServiceModel setId(Long id) {
        this.id = id;
        return this;
    }

    public String getUsername() {
        return this.username;
    }

    public TrainerUserInfoServiceModel setUsername(String username) {
        this.username = username;
        return this;
    }

    public Integer getUsersCount() {
        return this.usersCount;
    }

    public TrainerUserInfoServiceModel setUsersCount(Integer usersCount) {
        this.usersCount = usersCount;
        return this;
    }

    public TrainerType getType() {
        return this.type;
    }

    public TrainerUserInfoServiceModel setType(TrainerType type) {
        this.type = type;
        return this;
    }

    public Double getRating() {
        return this.rating;
    }

    public TrainerUserInfoServiceModel setRating(Double rating) {
        this.rating = rating;
        return this;
    }

    public String getProfilePicture() {
        return this.profilePicture;
    }

    public TrainerUserInfoServiceModel setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
        return this;
    }
}
