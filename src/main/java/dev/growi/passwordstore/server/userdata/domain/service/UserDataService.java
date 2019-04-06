package dev.growi.passwordstore.server.userdata.domain.service;

import dev.growi.passwordstore.server.userdata.dao.provider.UserDataProvider;
import dev.growi.passwordstore.server.userdata.dao.exception.UserNotFoundException;
import dev.growi.passwordstore.server.userdata.domain.model.IdWrapper;
import dev.growi.passwordstore.server.userdata.domain.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class UserDataService {

    @Autowired
    UserDataProvider userDataProvider;

    public User getById(IdWrapper<?> id) throws UserNotFoundException{

        return new User(userDataProvider.findUserById(id));
    }

    public User getByUserName(String userName) throws UserNotFoundException{

         return new User(userDataProvider.findUserByUserName(userName));
    }

    public User createUser(String userName, String password, UserDetails activeUser) throws UserNotFoundException {

         return new User(userDataProvider.createUser(userName, password, activeUser));
    }

}
