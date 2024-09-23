package com.animalus.securitytest.shiro;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

public class ShiroSecurityCondition implements Condition {
    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        // return BooleanUtils.isTrue(Boolean.valueOf(context.getEnvironment().getProperty("security.shiro")));
        return true;
    }
}
