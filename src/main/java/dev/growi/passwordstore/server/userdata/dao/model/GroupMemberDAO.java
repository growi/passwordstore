package dev.growi.passwordstore.server.userdata.dao.model;

import javax.crypto.EncryptedPrivateKeyInfo;
import java.time.Instant;

public interface GroupMemberDAO {

    GroupDAO getGroup();

    void setGroup(GroupDAO group);

    PrincipalDAO getMember();

    void setPrincipal(PrincipalDAO principal);

    int getPermissions();

    void setPermissions(int permissions);

    byte[] getSecret();

    void setSecret(byte[] secret);

    byte[] getGroupKey();

    void setGroupKey(byte[] groupKey);

    UserDAO getCreatedByUser();

    void setCreatedByUser(UserDAO createdByUser);

    Instant getCreatedStamp();

    void setCreatedStamp(Instant createdStamp);

    UserDAO getLastUpdatedByUser();

    void setLastUpdatedByUser(UserDAO updatedByuser);

    Instant getLastUpdatedStamp();

    void setLastUpdatedStamp(Instant lastUpdatedStamp);

}
