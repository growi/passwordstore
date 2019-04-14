package dev.growi.passwordstore.server.userdata.domain.service;

import dev.growi.passwordstore.server.userdata.dao.exception.CryptographyException;
import dev.growi.passwordstore.server.userdata.dao.model.UserDAO;
import dev.growi.passwordstore.server.userdata.dao.provider.UserDataProvider;
import dev.growi.passwordstore.server.userdata.dao.exception.UserNotFoundException;
import dev.growi.passwordstore.server.userdata.domain.model.IdWrapper;
import dev.growi.passwordstore.server.userdata.domain.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class UserDataService {

    private Map<IdWrapper<?>, User> beanCache = new HashMap<>();

    @Autowired
    private UserDataProvider userDataProvider;

    public User get(UserDAO user){
        if(!beanCache.containsKey(user.getUserId())){
            beanCache.put(user.getUserId(), new User(user));
        }
        return beanCache.get(user.getUserId());
    }

    public List<User> getAll() {
        return userDataProvider.findAll().stream().map(user -> get(user)).collect(Collectors.toList());
    }

    public User getById(IdWrapper<?> id) throws UserNotFoundException {

        return get(userDataProvider.findUserById(id));
    }

    public User getByUserName(String userName) throws UserNotFoundException {

        return get(userDataProvider.findUserByUserName(userName));
    }

    public User createUser(String userName, String password, UserDetails activeUser) throws UserNotFoundException, CryptographyException {

        return get(userDataProvider.createUser(userName, password, activeUser));
    }

}
