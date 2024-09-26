package com.animalus.securitytest;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import com.animalus.securitytest.api.AbstractController;
import com.animalus.securitytest.user.UserAccount;
import com.animalus.securitytest.user.UserAuth;

@RestControllerAdvice
public class ExceptionHandlerController {
    private UserAuth auth = Global.INST.auth;

    @ExceptionHandler(Exception.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public Exception handleException(final Exception ex, final WebRequest request) {
        UserAccount account;
        try {
            account = auth.getRemembered();
        } catch (SecurityException ex2) {
            account = null;
        }

        if (account != null && account.hasRole(AbstractController.ROLE_ADMIN)) {
            return ex;
        }

        //
        // Don't report stack trace to general user.
        //
        return new NotificationException(ex);
    }

    static class NotificationException extends RuntimeException {
        public NotificationException(final Exception cause) {
            super(cause.getMessage());
        }

        //
        // turns a stack trace you don't want the user to see into a string notification you want the user to see.
        //
        @Override
        public synchronized Throwable fillInStackTrace() {
            return this;
        }
    }
}
