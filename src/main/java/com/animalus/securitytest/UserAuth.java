package com.animalus.securitytest;

public interface UserAuth {

    User login(String username, String password, boolean rememberMe) throws Exception;

    void logout();

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
    UserAccount getRemembered();

    UserAccount getAuthenticated();

}
