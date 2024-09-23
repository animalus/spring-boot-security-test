package com.animalus.securitytest;

@FunctionalInterface
public interface UserActivity {
    void run(UserAccount account) throws Exception;
}
