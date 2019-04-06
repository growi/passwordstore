package dev.growi.passwordstore.server.userdata.dao.impl.ldap.dataprovider;

import dev.growi.passwordstore.server.userdata.dao.impl.ldap.configuration.LdapDatasourceCondition;
import dev.growi.passwordstore.server.userdata.dao.model.UserDAO;
import dev.growi.passwordstore.server.userdata.dao.provider.UserDataProvider;
import dev.growi.passwordstore.server.userdata.dao.exception.UserNotFoundException;
import dev.growi.passwordstore.server.userdata.domain.model.IdWrapper;
import org.springframework.context.annotation.Conditional;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@Conditional(LdapDatasourceCondition.class)
public class LdapUserDataProvider implements UserDataProvider {

    @Override
    public UserDAO findUserById(IdWrapper<?> id) throws UserNotFoundException {
        return null;
    }

    @Override
    public UserDAO findUserByUserName(String userName) throws UserNotFoundException {
        return null;
    }

    @Override
    public UserDAO createUser(String userName, String password, UserDetails activeUser) throws UserNotFoundException {
        return null;
    }
}
