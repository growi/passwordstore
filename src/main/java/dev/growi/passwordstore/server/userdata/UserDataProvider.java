package dev.growi.passwordstore.server.userdata;

public interface UserDataProvider {

    User finddUserById(Id<?> id);

    User finddUserByLogin(String login);

}
