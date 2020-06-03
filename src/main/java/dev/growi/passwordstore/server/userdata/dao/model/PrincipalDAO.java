package dev.growi.passwordstore.server.userdata.dao.model;

import dev.growi.passwordstore.server.shared.dao.model.MonitoredDAO;
import dev.growi.passwordstore.server.userdata.dao.impl.jpa.model.JpaGroupMember;

import java.util.Set;

public interface PrincipalDAO extends MonitoredDAO {

    Long getId();

    boolean isEnabled();

    void isEnabled(boolean enabled);

    byte[] getPublicKey();

    void setPublicKey(byte[] publicKey);

    Set<JpaGroupMember> getMemberships();

    void setMemberships(Set<JpaGroupMember> memberships);
}
