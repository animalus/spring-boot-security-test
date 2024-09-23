package com.animalus.securitytest;

public class User {
    private String salt;
    private String uuid;
    private String username;
    private String hashedPass;

    public User(String uuid, String username, String hashedPass, String salt) {
        this.uuid = uuid;
        this.username = username;
        this.hashedPass = hashedPass;
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
    
    public String getSalt() {
        return salt;
    }
}
