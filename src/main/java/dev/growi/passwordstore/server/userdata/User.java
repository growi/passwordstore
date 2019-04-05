package dev.growi.passwordstore.server.userdata;

import javax.crypto.EncryptedPrivateKeyInfo;

public interface User extends Principal{

    Id<?> getUserId();

    void setUserId(Id<?> userId);

    String getUserName();

    void setUserName();

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