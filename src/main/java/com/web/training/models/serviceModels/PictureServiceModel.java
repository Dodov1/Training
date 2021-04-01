package com.web.training.models.serviceModels;

public class PictureServiceModel {

    private Long id;
    private String location;

    public Long getId() {
        return this.id;
    }

    public PictureServiceModel setId(Long id) {
        this.id = id;
        return this;
    }

    public String getLocation() {
        return this.location;
    }

    public PictureServiceModel setLocation(String location) {
        this.location = location;
        return this;
    }
}
