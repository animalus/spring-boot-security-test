package com.animalus.securitytest.shiro;

import java.util.function.Function;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;

import com.animalus.securitytest.user.AccountStore;
import com.animalus.securitytest.user.User;
import com.animalus.securitytest.user.UserAccount;
import com.animalus.securitytest.user.UserAuth;

public class ShiroAuth implements UserAuth {
    private final AccountStore accountStore;

    public ShiroAuth(AccountStore accountStore) {
        this.accountStore = accountStore;
    }

    private Subject getSubject() {
        //
        // Get the current subject out of the current thread.
        //
        return SecurityUtils.getSubject();
    }

    private UserAccount fromSubject(Function<Subject, Boolean> filter) {
        Subject subject = getSubject();

        if (subject == null) {
            return null;
        }

        if (!filter.apply(subject)) {
            return null;
        }
        Object principal = subject == null ? null : subject.getPrincipal();

        if (principal == null) {
            return null;
        }

        String uuid = principal.toString();

        return accountStore.get(uuid);
    }

    @Override
    public User login(String username, String password, boolean rememberMe) throws Exception {
        UserAccount account = accountStore.getByName(username);
        User user = account.getUser();
        UsernamePasswordToken token = new UsernamePasswordToken(user.getUuid(), user.hashString(password), rememberMe);
        try {
            getSubject().login(token);
        } finally {
            token.clear();
        }
        return user;
    }

    @Override
    public void logout() {
        Subject subject = getSubject();
        if (subject != null) {
            //
            // NOTE: This will also invalidate the current session so no need
            // to do that explicitly.
            //
            subject.logout();
        }
    }

    /**
     * isAuthenticated() means that it was Authenticated during this session
     * and we are not relying on the rememberMe token. isRemembered() bascially says that
     * Shiro has obtained this token because we had the rememberMe token set BUT we haven't
     * authenticated the user **this** session (maybe the server was rebooted or we hit session timeout).
     * TODO: Break this out into secure getUserAccount and stanndard for use when we want to make
     * sure the user is who they say they are (e.g. to change their password, send highly secure information
     * like credit cards, etc.). In other words the rememberMe token is not sufficient for this job. In those
     * cases we rely only on the isAuthenticated method.
     */
    @Override
    public UserAccount getRemembered() {
        return fromSubject(subject -> subject.isAuthenticated() || subject.isRemembered());
    }

    @Override
    public UserAccount getAuthenticated() {
        return fromSubject(subject -> subject.isAuthenticated());
    }
}
