package dev.growi.passwordstore.server.userdata.dao.provider;

import dev.growi.passwordstore.server.shared.dao.provider.MonitoredDataProvider;
import dev.growi.passwordstore.server.shared.service.exception.CryptographyException;
import dev.growi.passwordstore.server.userdata.dao.exception.DatasourceException;
import dev.growi.passwordstore.server.userdata.dao.model.GroupMemberDAO;
import dev.growi.passwordstore.server.userdata.dao.model.UserDAO;
import dev.growi.passwordstore.server.userdata.dao.exception.UserNotFoundException;
import dev.growi.passwordstore.server.userdata.domain.exception.InvalidPasswordException;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;
import java.util.Set;

public interface UserDataProvider extends MonitoredDataProvider<Long, UserDAO> {

    void checkAdmin() throws CryptographyException;

    List<UserDAO> findAll();

    @Override
    UserDAO findById(Long id) throws UserNotFoundException;

    UserDAO findByUserName(String userName) throws UserNotFoundException;

    UserDAO create(String userName, String password) throws UserNotFoundException, DatasourceException;

    Set<GroupMemberDAO> findGroupMembershipsByUserId(Long id) throws UserNotFoundException;

    void changePassword(UserDAO user, String oldPassword, String newPassword) throws CryptographyException, InvalidPasswordException;

    UserDAO save(UserDAO user) throws UserNotFoundException;

    void deleteById(Long userId);
}
