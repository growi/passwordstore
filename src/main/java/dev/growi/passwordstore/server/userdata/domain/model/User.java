package dev.growi.passwordstore.server.userdata.domain.model;

import dev.growi.passwordstore.server.userdata.dao.model.UserDAO;

public class User extends Principal {

    private UserDAO userDAO;

    public User(UserDAO userDAO){
        this.userDAO = userDAO;
    }

}
