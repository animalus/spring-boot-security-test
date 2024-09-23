package com.animalus.securitytest;

import java.util.List;

public class User {
    private String salt;
    private String uuid;
    private String username;
    private String hashedPass;
    private List<String> roles;

    public User(String uuid, String username, String hashedPass, List<String> roles, String salt) {
        this.uuid = uuid;
        this.username = username;
        this.hashedPass = hashedPass;
        this.roles = roles;
        this.salt = salt;
    }

    public String getUuid() {
        return uuid;
    }

    public String getUsername() {
        return username;
    }

    public String getHashedPass() {
        return hashedPass;
    }

    public List<String> getRoles() {
        return roles;
    }
    
    public String getSalt() {
        return salt;
    }
}
