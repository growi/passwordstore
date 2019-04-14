package dev.growi.passwordstore.server.userdata.dao.impl.ldap.dataprovider;

import dev.growi.passwordstore.server.userdata.dao.exception.CryptographyException;
import dev.growi.passwordstore.server.userdata.dao.impl.ldap.configuration.LdapDatasourceCondition;
import dev.growi.passwordstore.server.userdata.dao.model.GroupMemberDAO;
import dev.growi.passwordstore.server.userdata.dao.model.UserDAO;
import dev.growi.passwordstore.server.userdata.dao.provider.UserDataProvider;
import dev.growi.passwordstore.server.userdata.dao.exception.UserNotFoundException;
import dev.growi.passwordstore.server.userdata.domain.model.IdWrapper;
import org.springframework.context.annotation.Conditional;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@Conditional(LdapDatasourceCondition.class)
public class LdapUserDataProvider implements UserDataProvider {

    @Override
    public void checkAdmin() {

    }

    @Override
    public List<UserDAO> findAll(){
        return null;
    }

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

    @Override
    public Set<GroupMemberDAO> findGroupMembershipsByUserId(IdWrapper<?> userId) throws UserNotFoundException {
        return null;
    }

    @Override
    public void changePassword(UserDAO user, String oldPassword, String newPassword) throws CryptographyException {

    }
}
