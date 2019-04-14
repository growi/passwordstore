package dev.growi.passwordstore.server.userdata.dao.provider;

import dev.growi.passwordstore.server.userdata.dao.exception.CryptographyException;
import dev.growi.passwordstore.server.userdata.dao.model.GroupMemberDAO;
import dev.growi.passwordstore.server.userdata.dao.model.UserDAO;
import dev.growi.passwordstore.server.userdata.dao.exception.UserNotFoundException;
import dev.growi.passwordstore.server.userdata.domain.model.IdWrapper;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;
import java.util.Set;

public interface UserDataProvider {

    void checkAdmin() throws CryptographyException;

    List<UserDAO> findAll();

    UserDAO findUserById(IdWrapper<?> id) throws UserNotFoundException;

    UserDAO findUserByUserName(String userName) throws UserNotFoundException;

    UserDAO createUser(String userName, String password, UserDetails activeUser) throws UserNotFoundException, CryptographyException;

    Set<GroupMemberDAO> findGroupMembershipsByUserId(IdWrapper<?> userId) throws UserNotFoundException;

    void changePassword(UserDAO user, String oldPassword, String newPassword) throws CryptographyException;
}
