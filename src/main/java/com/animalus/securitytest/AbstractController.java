package com.animalus.securitytest;

import java.util.function.Supplier;

public abstract class AbstractController {
    protected UserAuth auth = Global.INST.auth;
    final static String ROLE_ADMIN = "admin";

    protected Supplier<UserAccount> USER_NOT_REQ = () -> auth.getRemembered();

    protected Supplier<UserAccount> USER_REQ = () -> {
        UserAccount user = auth.getRemembered();
        if (user == null) {
            throw new AuthorizationException("Not logged in.");
        }
        return user;
    };

    //
    // To be used when we are doing something very secure, like password updating or fincial stuff, or ...
    //
    protected Supplier<UserAccount> USER_AUTHENTICATED = () -> {
        UserAccount user = auth.getAuthenticated();
        if (user == null) {
            throw new AuthorizationException("Not logged in.");
        }
        return user;
    };

    private UserAccount authCheck(AuthFunction authFn) {
        return USER_REQ.get().authCheck(authFn);
    }


    protected Supplier<UserAccount> USER_HAS_ROLE(String role) {
        return () -> {
            return authCheck(new AuthFunction() {
                @Override
                public Boolean apply(UserAccount account) {
                    return account.hasRole(ROLE_ADMIN) || account.hasRole(role);
                }
            });
        };
    }

    protected Supplier<UserAccount> USER_ADMIN = () -> {
        return authCheck(new AuthFunction() {
            @Override
            public Boolean apply(UserAccount account) {
                return account.hasRole(ROLE_ADMIN);
            }
        });
    };

    protected void userActivity(Supplier<UserAccount> userFn, UserActivity func) throws Exception {
        func.run(userFn.get());
    }

    protected <T> T userFunc(Supplier<UserAccount> userFn, UserFunction<T> func) throws Exception {
        return func.run(userFn.get());
    }
}
