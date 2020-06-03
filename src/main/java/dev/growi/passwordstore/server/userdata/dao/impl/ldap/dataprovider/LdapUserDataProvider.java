package dev.growi.passwordstore.server.userdata.dao.impl.ldap.dataprovider;

import dev.growi.passwordstore.server.shared.dao.exception.EntityNotFoundException;
import dev.growi.passwordstore.server.shared.dao.model.MonitoredDAO;
import dev.growi.passwordstore.server.shared.service.exception.CryptographyException;
import dev.growi.passwordstore.server.userdata.dao.exception.DatasourceException;
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
    public UserDAO create() throws DatasourceException {
        return null;
    }

    @Override
    public UserDAO findById(Long id) throws EntityNotFoundException {
        return null;
    }

    @Override
    public List<UserDAO> findAll(){
        return null;
    }

    @Override
    public UserDAO findById(Long id) throws UserNotFoundException {
        return null;
    }

    @Override
    public UserDAO findByUserName(String userName) throws UserNotFoundException {
        return null;
    }

    @Override
    public UserDAO create(String userName, String password) throws UserNotFoundException {
        return null;
    }

    @Override
    public Set<GroupMemberDAO> findGroupMembershipsByUserId(Long userId) throws UserNotFoundException {
        return null;
    }

    @Override
    public void changePassword(UserDAO user, String oldPassword, String newPassword) throws CryptographyException {

    }

    @Override
    public UserDAO save(UserDAO user) throws UserNotFoundException {
        return null;
    }

    @Override
    public void deleteById(Long userId) {

    }

    @Override
    public MonitoredDAO setCreatedInfo(MonitoredDAO monitored) throws UserNotFoundException {
        return null;
    }

    @Override
    public MonitoredDAO setLastUpdatedInfo(MonitoredDAO monitored) throws UserNotFoundException {
        return null;
    }
}
