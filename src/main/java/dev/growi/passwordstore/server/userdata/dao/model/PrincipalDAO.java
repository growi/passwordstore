package dev.growi.passwordstore.server.userdata.dao.model;

import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.time.Instant;

public interface PrincipalDAO {

    boolean isEnabled();

    void isEnabled(boolean enabled);

    byte[] getPublicKey();

    void setPublicKey(byte[] publicKey);

    UserDAO getCreatedByUser();

    void setCreatedByUser(UserDAO createdByUser);

    Instant getCreatedStamp();

    void setCreatedStamp(Instant createdStamp);

    UserDAO getLastUpdatedByUser();

    void setLastUpdatedByUser(UserDAO lastUpdatedByUser);

    Instant getLastUpdatedStamp();

    void setLastUpdatedStamp(Instant lastUpdatedStamp);

}
