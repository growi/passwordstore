package dev.growi.passwordstore.server.userdata.dao.model;

import dev.growi.passwordstore.server.userdata.dao.impl.jpa.model.JpaUserMember;
import dev.growi.passwordstore.server.userdata.domain.model.IdWrapper;

import javax.crypto.EncryptedPrivateKeyInfo;
import java.io.IOException;
import java.util.Collection;

public interface UserDAO extends PrincipalDAO {

    Long getId();

    String getUserName();

    void setUserName(String userName);

    String getPassword();

    boolean isAccountExpired();

    void isAccountExpired(boolean accountExpired);

    boolean isAccountLocked();

    void isAccountLocked(boolean accountLocked);

    boolean isCredentialsExpired();

    void isCredentialsExpired(boolean credentialsExpired);

    byte[] getPrivateKey();

    void setPrivateKey(byte[] privateKey);

}