package com.animalus.securitytest;

import java.util.HashMap;
import java.util.Map;

public class AccountStore {
    private Map<String, User> uuidMap = new HashMap<>();
    private Map<String, User> usernameMap = new HashMap<>();
    private final CookieConfig cookieConfig;

    public AccountStore() {
        cookieConfig = new CookieConfig();
        cookieConfig.setCipherKey("cipher_key");
        cookieConfig.setRememberMeDays(30);
        cookieConfig.setSameSiteOption(null);
    }

    public User get(String uuid) {
        User user = uuidMap.get(uuid);
        if (user == null) {
            //
            // NOTE: Would get from database.
            //
            user = new User(uuid, "username", "******", null, "**********");
            uuidMap.put(uuid, user);
        }
        return user;
    }

    public User getByName(String username) {
        User user = usernameMap.get(username);
        if (user == null) {
            //
            // NOTE: Would get from database.
            //
            user = new User("uuid", username, "******", null, "*******");
            usernameMap.put(username, user);
        }
        return user;
    }

    public CookieConfig getCookieConfig() {
        return cookieConfig;
    }
}
