package com.animalus.securitytest;

import java.util.List;

import org.apache.shiro.authz.AuthorizationException;

public class UserAccount {
    private User user;
    private List<String> roles;

    public UserAccount(User user, List<String> roles) {
        this.user = user;
        this.roles = roles;
    }

    public User getUser() {
        return user;
    }

    public List<String> getRoles() {
        return roles;
    }

    public boolean hasRole(final String role) {
        if (roles == null) {
            return false;
        }

        return roles.contains(role);
    }

    public UserAccount authCheck(final AuthFunction authFn) {
        if (!authFn.apply(this)) {
            throw new AuthorizationException("Unauthorized Access");
        }
        return this;
    }

    public void checkPassword(final String pwd) {
        if (!getUser().checkPassword(pwd)) {
            throw new AuthorizationException("Invalid credentials");
        }
    }
}
