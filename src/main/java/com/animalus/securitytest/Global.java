package com.animalus.securitytest;

import com.animalus.securitytest.user.AccountStore;
import com.animalus.securitytest.user.UserAuth;

public enum Global {
    INST;

    public AccountStore accountStore;
    public UserAuth auth;
}
