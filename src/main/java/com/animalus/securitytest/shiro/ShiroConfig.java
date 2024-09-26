package com.animalus.securitytest.shiro;

import java.util.Arrays;
import java.util.Base64;
import java.util.Optional;

import org.apache.shiro.realm.Realm;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.mgt.WebSecurityManager;
import org.apache.shiro.web.servlet.Cookie;
import org.apache.shiro.web.servlet.Cookie.SameSiteOptions;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import com.animalus.securitytest.CookieConfig;
import com.animalus.securitytest.Global;
import com.animalus.securitytest.user.AccountStore;

/**
 * Since upgrading my Spring Boot to 3.3.0 and Shiro to 1.12.0 I now get the following warning in my log file ...
 *     Bean 'shiroConfig' of type [com.animalus.core.http.ShiroConfig$$SpringCGLIB$$0] is not eligible
 *     for getting processed by all BeanPostProcessors (for example: not eligible for auto-proxying).
 *     Is this bean getting eagerly injected into a currently created BeanPostProcessor []?
 *     Check the corresponding BeanPostProcessor declaration and its dependencies.
 * But there is no other issue. According to this we can just ignore it, but it is annoying.
 *     https://stackoverflow.com/questions/12358783/class-not-eligible-for-getting-processed-by-all-beanpostprocessors
 */
@Configuration
@Conditional(ShiroSecurityCondition.class)
public class ShiroConfig {

    //
    // As the shiro documents indicate this just makes it so that you
    // can defer this till late in the Spring startup and be sure certain things are taken care of.
    //
    @Bean
    static public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }

    @Bean
    @DependsOn("lifecycleBeanPostProcessor")
    static public WebSecurityManager securityManager() {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();

        AccountStore accountStore = Global.INST.accountStore;
        Realm realm = new BasicRealm(accountStore);
        securityManager.setRealm(realm);

        //
        // NOTE: Setting SameSite = None on cookie type so that I can debug against beta.
        // If I don't do this, then I can't run localhost against beta.critterpot because of new
        // cookie security that defaults to SameSite=LAX. Not exactly sure what that means but effectively acts like
        // STRICT in my opinion (the other option). Basiclly it doesn't allow the cookie to be set unless its
        // the same site. Supposedly you need another something to make this more secure, but I'm unclear on that.
        // TODO: Investigate above security thing, might be automatically handled by Shiro?
        //
        DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
        sessionManager.getSessionIdCookie().setSameSite(SameSiteOptions.NONE);
        securityManager.setSessionManager(sessionManager);

        //
        // cipher key created with...Rather than use the hard-coded one in Shiro that
        // anyone can read in the source-code.
        //
        // AesCipherService aes = new AesCipherService();
        // byte[] key = aes.generateNewKey().getEncoded();
        // String base64 = Base64.encodeToString(key);
        //
        CookieConfig cookieConfig = accountStore.getCookieConfig(); 

        CookieRememberMeManager rmm = new CookieRememberMeManager();
        rmm.setCipherKey(Base64.getDecoder().decode(cookieConfig.getCipherKey()));
        Cookie cookie = rmm.getCookie();

        Optional<SameSiteOptions> sso = Arrays.stream(SameSiteOptions.values())
                                              .filter(item -> item.name()
                                                                  .equalsIgnoreCase(cookieConfig.getSameSiteOption()))
                                              .findFirst();
        if (sso.isPresent()) {
            cookie.setSameSite(sso.get());
        }
        //
        // NOTE: One year is the default if we don't set it inside the shiro Cookie.
        //
        Integer rememberMeTimeoutDays = cookieConfig.getRememberMeDays();
        if (rememberMeTimeoutDays != null) {
            cookie.setMaxAge(60 * 60 * 24 * rememberMeTimeoutDays);
        }

        securityManager.setRememberMeManager(rmm);

        return securityManager;
    }

    @Bean
    public ShiroFilterFactoryBean shiroFilter() {
        ShiroFilterFactoryBean shiroFilter = new ShiroFilterFactoryBean();

        //
        // NOTE: Had to comment out the code that protects the admin endpoint based on the admin role
        //  because it was interfering with the CORS filter and thus did not allow cross-domain POSTs.
        // A problem after we broke out the admin client app to it's own domain.
        //
        //        Map<String, String> filterChain = new HashMap<>();
        //        filterChain.put("/admin/**", "roles[admin]");
        //
        //        shiroFilter.setFilterChainDefinitionMap(filterChain);
        //
        //        Map<String, Filter> filters = new HashMap<>();
        //        filters.put("roles", new AuthFilter());
        //
        //        shiroFilter.setFilters(filters);

        shiroFilter.setSecurityManager(securityManager());

        return shiroFilter;
    }

}
