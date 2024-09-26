package com.animalus.securitytest.user;

public interface AuthFunction {
    Boolean apply(UserAccount account);
}
