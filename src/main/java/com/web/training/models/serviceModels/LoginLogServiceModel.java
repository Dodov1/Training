package com.web.training.models.serviceModels;

import java.time.LocalDateTime;

public class LoginLogServiceModel {

    private String message;
    private LocalDateTime time;

    public String getMessage() {
        return this.message;
    }

    public LoginLogServiceModel setMessage(String message) {
        this.message = message;
        return this;
    }

    public LocalDateTime getTime() {
        return this.time;
    }

    public LoginLogServiceModel setTime(LocalDateTime time) {
        this.time = time;
        return this;
    }
}
