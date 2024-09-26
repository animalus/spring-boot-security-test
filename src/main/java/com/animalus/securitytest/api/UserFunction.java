package com.animalus.securitytest.api;

import com.animalus.securitytest.user.UserAccount;

@FunctionalInterface
public interface UserFunction<T> {
    T run(UserAccount account) throws Exception;
}
