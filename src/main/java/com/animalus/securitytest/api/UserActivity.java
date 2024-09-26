package com.animalus.securitytest.api;

import com.animalus.securitytest.user.UserAccount;

@FunctionalInterface
public interface UserActivity {
    void run(UserAccount account) throws Exception;
}
