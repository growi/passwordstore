package dev.growi.passwordstore.server.userdata.dao.impl.jpa.configuration;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotatedTypeMetadata;

public class JpaDatasourceCondition implements Condition {

    @Override
    public boolean matches(ConditionContext context,
                           AnnotatedTypeMetadata metadata) {
        Environment env = context.getEnvironment();

        return null != env
                && "JPA".equalsIgnoreCase(env.getProperty("passwordstore.userdata.datasource.type"));
    }
}
