package dev.growi.passwordstore.server.userdata.dao.impl.ldap.configuration;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotatedTypeMetadata;

public class LdapDatasourceCondition implements Condition {

    @Override
    public boolean matches(ConditionContext context,
                           AnnotatedTypeMetadata metadata) {
        Environment env = context.getEnvironment();

        return null != env
                && "LDAP".equalsIgnoreCase(env.getProperty("passwordstore.userdata.datasource.type"));
    }
}
