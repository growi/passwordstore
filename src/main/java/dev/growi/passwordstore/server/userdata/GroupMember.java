package dev.growi.passwordstore.server.userdata;

import javax.crypto.EncryptedPrivateKeyInfo;
import java.time.Instant;

public interface GroupMember {

    Group getGroup();

    void setGroup(Group group);

    Principal getMember();

    void setPrincipal(Principal principal);

    int getPermissions();

    void setPermissions(int permissions);

    byte[] getSecret();

    void setSecret(byte[] secret);

    EncryptedPrivateKeyInfo getGroupKey();

    void setGroupKey(EncryptedPrivateKeyInfo groupKey);

    User getCreatedByUser();

    void setCreatedByUser(User createdByUser);

    Instant getCreatedStamp();

    void setCreatedStamp(Instant createdStamp);

    User getUpdatedByUser();

    void setUpdatedByUsr(User updatedByuser);

    Instant getLastUpdatedStamp();

    void setLastUpdatedStamp(Instant lastUpdatedStamp);
}
