package com.animalus.securitytest.shiro;

import java.util.HashSet;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.authc.credential.SimpleCredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import com.animalus.securitytest.AccountStore;
import com.animalus.securitytest.User;

public class BasicRealm extends AuthorizingRealm {
    private static final String REALM_NAME = "BASIC";
    private final AccountStore accountStore;
 
    public BasicRealm(final AccountStore accountStore) {
        super();
        this.accountStore = accountStore;
    }

    @Override
    protected AuthenticationInfo
              doGetAuthenticationInfo(final AuthenticationToken token) throws AuthenticationException {
        UsernamePasswordToken upToken = (UsernamePasswordToken) token;
        String uuid = upToken.getUsername();

        if (StringUtils.isBlank(uuid)) {
            throw new AuthenticationException("No user specified.");
        }

        User user = accountStore.get(uuid);

        if (user == null) {
            throw new AuthenticationException("No account found for user [%s]".formatted(uuid));
        }

        if (StringUtils.isBlank(user.getHashedPass())) {
            throw new AuthenticationException("No password set.");
        }

        SimpleAuthenticationInfo info;
        info = new SimpleAuthenticationInfo(uuid, user.getHashedPass().toCharArray(), REALM_NAME);

        CredentialsMatcher matcher = new SimpleCredentialsMatcher();
        if (!matcher.doCredentialsMatch(upToken, info)) {
            throw new AuthenticationException("Invalid credentials.");
        }

        return info;
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(final PrincipalCollection principals) {
        String uuid = (String) principals.getPrimaryPrincipal();

        if (uuid == null) {
            return new SimpleAuthorizationInfo();
        }

        User user = accountStore.get(uuid);

        if (user == null) {
            return new SimpleAuthorizationInfo();
        }

        return new SimpleAuthorizationInfo(new HashSet<>(user.getRoles()));
    }
}
