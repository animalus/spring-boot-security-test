package com.animalus.securitytest;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;

import com.animalus.securitytest.shiro.ShiroAuth;

@SpringBootApplication(scanBasePackages = { "com.animalus.securitytest" })
// exclude = { SecurityAutoConfiguration.class })
public class TestBootApplication {
    public static void main(String[] args) {
        SpringApplicationBuilder builder = new SpringApplicationBuilder(TestBootApplication.class);
        builder.listeners((ApplicationListener<ApplicationEnvironmentPreparedEvent>) event -> {
            AccountStore accountStore = new AccountStore();
            accountStore.addAccount("uuid1", "bob", "12345678", "salt1", null);
            accountStore.addAccount("uuid2", "admin", "12345678", "salt2", "admin");
            accountStore.addAccount("uuid3", "sponsor", "12345678", "salt3", "sponsor");
            Global.INST.accountStore = accountStore;
            Global.INST.auth = new ShiroAuth(accountStore);
        }, (ApplicationListener<ContextClosedEvent>) event -> {
            // close
        }, (ApplicationListener<ApplicationReadyEvent>) event -> {
            System.out.println("App is ready!");
        })
               .run(args);
    }
}
