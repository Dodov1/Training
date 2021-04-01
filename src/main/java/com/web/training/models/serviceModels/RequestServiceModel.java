package com.web.training.models.serviceModels;

public class RequestServiceModel {

    private Long id;
    private String username;
    private String profilePicture;

    public Long getId() {
        return this.id;
    }

    public RequestServiceModel setId(Long id) {
        this.id = id;
        return this;
    }

    public String getUsername() {
        return this.username;
    }

    public RequestServiceModel setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getProfilePicture() {
        return this.profilePicture;
    }

    public RequestServiceModel setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
        return this;
    }
}
