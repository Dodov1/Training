package com.web.training.models.serviceModels;

import com.web.training.models.enums.TrainerType;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class TrainerServiceModel {

    private Long id;
    private String username;
    private String firstName;
    private String lastName;
    private TrainerType type;

    public String getUsername() {
        return this.username;
    }

    public TrainerServiceModel setUsername(String username) {
        this.username = username;
        return this;
    }

    public Long getId() {
        return this.id;
    }

    public TrainerServiceModel setId(Long id) {
        this.id = id;
        return this;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public TrainerServiceModel setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public String getLastName() {
        return this.lastName;
    }

    public TrainerServiceModel setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public TrainerType getType() {
        return this.type;
    }

    public TrainerServiceModel setType(TrainerType type) {
        this.type = type;
        return this;
    }
}
