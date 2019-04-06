package dev.growi.passwordstore.server.userdata.dao.provider;

import dev.growi.passwordstore.server.userdata.dao.model.UserDAO;
import dev.growi.passwordstore.server.userdata.dao.exception.UserNotFoundException;
import dev.growi.passwordstore.server.userdata.domain.model.IdWrapper;
import org.springframework.security.core.userdetails.UserDetails;

public interface UserDataProvider {

    UserDAO findUserById(IdWrapper<?> id) throws UserNotFoundException;

    UserDAO findUserByUserName(String userName) throws UserNotFoundException;

    UserDAO createUser(String userName, String password, UserDetails activeUser) throws UserNotFoundException;

}
