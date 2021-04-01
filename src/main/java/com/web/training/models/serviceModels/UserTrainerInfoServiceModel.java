package com.web.training.models.serviceModels;

public class UserTrainerInfoServiceModel {

    private Long id;
    private String username;
    private Integer trainingCount;
    private Integer age;
    private Integer height;
    private String profilePicture;

    public String getProfilePicture() {
        return this.profilePicture;
    }

    public UserTrainerInfoServiceModel setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
        return this;
    }

    public Integer getAge() {
        return this.age;
    }

    public UserTrainerInfoServiceModel setAge(Integer age) {
        this.age = age;
        return this;
    }

    public Integer getHeight() {
        return this.height;
    }

    public UserTrainerInfoServiceModel setHeight(Integer height) {
        this.height = height;
        return this;
    }

    public Long getId() {
        return this.id;
    }

    public UserTrainerInfoServiceModel setId(Long id) {
        this.id = id;
        return this;
    }

    public String getUsername() {
        return this.username;
    }

    public UserTrainerInfoServiceModel setUsername(String username) {
        this.username = username;
        return this;
    }

    public Integer getTrainingCount() {
        return this.trainingCount;
    }

    public UserTrainerInfoServiceModel setTrainingCount(Integer trainingCount) {
        this.trainingCount = trainingCount;
        return this;
    }
}
