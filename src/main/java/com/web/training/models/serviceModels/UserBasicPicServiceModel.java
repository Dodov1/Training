package com.web.training.models.serviceModels;

public class UserBasicPicServiceModel {

    private Long id;
    private String firstName;
    private String username;
    private String lastName;
    private String profilePicture;

    public Long getId() {
        return this.id;
    }

    public UserBasicPicServiceModel setId(Long id) {
        this.id = id;
        return this;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public UserBasicPicServiceModel setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public String getUsername() {
        return this.username;
    }

    public UserBasicPicServiceModel setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getLastName() {
        return this.lastName;
    }

    public UserBasicPicServiceModel setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public String getProfilePicture() {
        return this.profilePicture;
    }

    public UserBasicPicServiceModel setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
        return this;
    }
}
