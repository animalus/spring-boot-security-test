package com.animalus.securitytest;

import java.util.HashMap;
import java.util.Map;

public class AccountStore {
    private Map<String, UserAccount> uuidMap = new HashMap<>();
    private Map<String, UserAccount> usernameMap = new HashMap<>();
    private final CookieConfig cookieConfig;

    public AccountStore() {
        cookieConfig = new CookieConfig();
        cookieConfig.setCipherKey("cipher_key");
        cookieConfig.setRememberMeDays(30);
        cookieConfig.setSameSiteOption(null);
    }

    public UserAccount get(String uuid) {
        UserAccount account = uuidMap.get(uuid);
        if (account == null) {
            //
            // NOTE: Would get from database.
            //
            account = new UserAccount(new User(uuid, "username", "******", "**********"), null);
            uuidMap.put(uuid, account);
        }
        return account;
    }

    public UserAccount getByName(String username) {
        UserAccount account = usernameMap.get(username);
        if (account == null) {
            //
            // NOTE: Would get from database.
            //
            account = new UserAccount(new User("uuid", username, "******", "*******"), null);
            usernameMap.put(username, account);
        }
        return account;
    }

    public CookieConfig getCookieConfig() {
        return cookieConfig;
    }
}
