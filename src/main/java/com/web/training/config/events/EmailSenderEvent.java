package com.web.training.config.events;

import com.web.training.models.entities.User;
import org.springframework.context.ApplicationEvent;

public class EmailSenderEvent extends ApplicationEvent {

    private final User user;

    public EmailSenderEvent(Object source, User user) {
        super(source);
        this.user = user;
    }

    public User getUser() {
        return this.user;
    }
}
