package com.animalus.securitytest;

public class AuthorizationException extends RuntimeException {
    public AuthorizationException() {
        super("Unauthorized access.");
    }

    public AuthorizationException(final String message) {
        super(message);
    }

    public static AuthorizationException invalidCredentials() {
        return new AuthorizationException("Invalid credentials.");
    }
}
