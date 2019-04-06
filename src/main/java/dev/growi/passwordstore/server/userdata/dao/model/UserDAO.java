package dev.growi.passwordstore.server.userdata.dao.model;

import dev.growi.passwordstore.server.userdata.domain.model.IdWrapper;

import javax.crypto.EncryptedPrivateKeyInfo;

public interface UserDAO extends PrincipalDAO {

    IdWrapper<?> getUserId();

    String getUserName();

    void setUserName(String userName);

    String getPassword();

    void setPassword(String password);

    boolean isAccountExpired();

    void isAccountExpired(boolean accountExpired);

    boolean isAccountLocked();

    void isAccountLocked(boolean accountLocked);

    boolean isCredentialsExpired();

    void isCredentialsExpired(boolean credentialsExpired);

    EncryptedPrivateKeyInfo getPrivateKey();

    void setPrivateKey(EncryptedPrivateKeyInfo privateKey);

}