package dev.growi.passwordstore.server.carddata.dao.model;

import dev.growi.passwordstore.server.carddata.dao.impl.jpa.model.JpaAccessControlList;
import dev.growi.passwordstore.server.shared.dao.model.MonitoredDAO;
import dev.growi.passwordstore.server.userdata.dao.model.PrincipalDAO;

public interface AccessControlEntryDAO extends MonitoredDAO {
    Long getId();

    void setId(Long id);

    JpaAccessControlList getAcl();

    void setAcl(JpaAccessControlList acl);

    PrincipalDAO getPrincipal();

    void setPrincipal(PrincipalDAO principal);

    byte[] getSecret();

    void setSecret(byte[] secret);

    int getPermission();

    void setPermission(int permission);
}
