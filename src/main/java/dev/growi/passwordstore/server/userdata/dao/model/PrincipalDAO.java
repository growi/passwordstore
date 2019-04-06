package dev.growi.passwordstore.server.userdata.dao.model;

import java.security.PublicKey;
import java.time.Instant;

public interface PrincipalDAO {

    boolean isEnabled();

    void isEnabled(boolean enabled);

    PublicKey getPublicKey();

    void setPublicKey(PublicKey publicKey);

    UserDAO getCreatedByUser();

    void setCreatedByUser(UserDAO createdByUser);

    Instant getCreatedStamp();

    void setCreatedStamp(Instant createdStamp);

    UserDAO getLastUpdatedByUser();

    void setLastUpdatedByUser(UserDAO lastUpdatedByUser);

    Instant getLastUpdatedStamp();

    void setLastUpdatedStamp(Instant lastUpdatedStamp);

}
