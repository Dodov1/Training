package com.web.training.models.security;

import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class LoginUserSecurityModel {

    private Long userId;
    private Long trainerId;
    private Collection<GrantedAuthority> authorities;

    public Long getTrainerId() {
        return this.trainerId;
    }

    public LoginUserSecurityModel setTrainerId(Long trainerId) {
        this.trainerId = trainerId;
        return this;
    }

    public Long getUserId() {
        return this.userId;
    }

    public LoginUserSecurityModel setUserId(Long userId) {
        this.userId = userId;
        return this;
    }

    public Collection<GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    public LoginUserSecurityModel setAuthorities(Collection<GrantedAuthority> authorities) {
        this.authorities = authorities;
        return this;
    }
}
