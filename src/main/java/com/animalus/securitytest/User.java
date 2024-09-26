package com.animalus.securitytest;

import org.apache.shiro.crypto.hash.Sha512Hash;

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

    public String hashString(final String text) {
        return new Sha512Hash(text, salt, 200000).toHex();
    }

    public boolean checkPassword(String pwd) {
        return hashedPass.equals(hashString(pwd));
    }
}
