package com.animalus.securitytest;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.shiro.crypto.hash.Sha512Hash;

public class AccountStore {
    private Map<String, UserAccount> uuidMap = new HashMap<>();
    private Map<String, UserAccount> usernameMap = new HashMap<>();
    private final CookieConfig cookieConfig;

    public AccountStore() {
        cookieConfig = new CookieConfig();
        cookieConfig.setCipherKey("D9E14222DD7AF83F8BBC74F3");
        cookieConfig.setRememberMeDays(30);
        cookieConfig.setSameSiteOption(null);
    }

    public static String hashString(String text, String salt) {
        return new Sha512Hash(text, salt, 200000).toHex();
    }

    public void addAccount(String uuid, String username, String password, String salt, String role) {
        addAccount(new UserAccount(new User(uuid, username, hashString(password, salt), salt),
                   role == null ? null : Collections.singletonList(role)));
    }

    public void addAccount(UserAccount account) {
        uuidMap.put(account.getUser().getUuid(), account);
        usernameMap.put(account.getUser().getUsername(), account);
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
