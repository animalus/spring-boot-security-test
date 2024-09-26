package com.animalus.securitytest.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.animalus.securitytest.user.User;


@RestController
@RequestMapping("user")
public class UserController extends AbstractController {
    private WebUser toWeb(User user) {
        return user == null ? null : new WebUser(user.getUuid(), user.getUsername());
    }

    @PostMapping("login")
    public WebUser login(@RequestBody final LoginAttempt loginAttempt) throws Exception {
        return toWeb(auth.login(loginAttempt.username, loginAttempt.password, loginAttempt.rememberMe));
    }

    @PostMapping("logout")
    public void logout() {
        auth.logout();
    }

    @GetMapping("checklogin")
    public WebUser checklogin() throws Exception {
        return userFunc(USER_NOT_REQ, (account) -> {
            return toWeb(account == null ? null : account.getUser());
        });
    }

    @PostMapping("changepw")
    public void changePw(@RequestBody final PasswordChange request) throws Exception {
        userActivity(USER_REQ, (account) -> {
            account.checkPassword(request.passwordOld);

            // UserFactory.resetPassword(db, account.getUser(), request.password);
        });
    }

    static record LoginAttempt(String username, String password, boolean rememberMe) {
    }

    static record PasswordChange(String passwordOld, String password) {
    }

    static record WebUser(String uuid, String username){}
}
