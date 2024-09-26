package com.animalus.securitytest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("coffee")
public class TestController extends AbstractController {
    private Coffee getUserCoffee() {
        return new Coffee("latte", CoffeeSize.MEDIUM);
    }

    @GetMapping("public")
    public Coffee getPublic() throws Exception {
        return userFunc(USER_NOT_REQ, account -> {
            if (account == null) {
                return new Coffee("drip", CoffeeSize.SMALL);
            }
            return getUserCoffee();
        });
    }

    @GetMapping("user")
    public Coffee getUser() throws Exception {
        return userFunc(USER_REQ, account -> getUserCoffee());
    }

    @GetMapping("admin")
    public Coffee getAdmin() throws Exception {
        return userFunc(USER_ADMIN, account -> new Coffee("cappucino", CoffeeSize.LARGE));
    }

    @GetMapping("sponsor")
    public Coffee getSponsor() throws Exception {
        return userFunc(USER_HAS_ROLE("sponsor"), account -> new Coffee("americano", CoffeeSize.LARGE));
    }

    static enum CoffeeSize {
        SMALL, MEDIUM, LARGE;
    }

    static record Coffee(String type, CoffeeSize size) {
    }
}
