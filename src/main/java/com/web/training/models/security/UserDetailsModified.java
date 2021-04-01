package com.web.training.models.security;

import com.web.training.models.entities.Trainer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

public class UserDetailsModified extends User {

    private Long id;
    private Trainer trainer;
    private boolean isVerified;

    public boolean isVerified() {
        return this.isVerified;
    }

    public UserDetailsModified setIsEVerified(boolean isVerified) {
        this.isVerified = isVerified;
        return this;
    }

    public Trainer getTrainer() {
        return this.trainer;
    }

    public UserDetailsModified setTrainer(Trainer trainer) {
        this.trainer = trainer;
        return this;
    }

    public UserDetailsModified(String username, String password, Collection<? extends GrantedAuthority> authorities, Long id, Trainer trainer, boolean isVerified) {
        super(username, password, authorities);
        this.id = id;
        this.trainer = trainer;
        this.isVerified = isVerified;
    }

    public Long getId() {
        return this.id;
    }

    public UserDetailsModified setId(Long id) {
        this.id = id;
        return this;
    }
}
