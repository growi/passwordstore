package dev.growi.passwordstore.server.api.message;

import dev.growi.passwordstore.server.userdata.domain.model.User;

public class NewUser {

    private User user;
    private String password;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
