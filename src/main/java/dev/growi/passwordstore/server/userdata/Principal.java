package dev.growi.passwordstore.server.userdata;

import java.security.PublicKey;
import java.time.Instant;

public interface Principal {

    boolean isEnabled();

    void isEnabled(boolean enabled);

    PublicKey getPublicKey();

    void setPublicKey(PublicKey publicKey);

    User getCreatedByUser();

    void setCreatedByUser(User createdByUser);

    Instant getCreatedStamp();

    void setCreatedStamp(Instant createdStamp);

    User getLastUpdatedByUser();

    void setLastUpdatedByUsr(User lastUpdatedByUser);

    Instant getLastUpdatedStamp();

    void setLastUpdatedStamp(Instant lastUpdatedStamp);

}
