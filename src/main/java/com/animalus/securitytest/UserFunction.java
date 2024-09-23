package com.animalus.securitytest;

@FunctionalInterface
public interface UserFunction<T> {
    T run(UserAccount account) throws Exception;
}
