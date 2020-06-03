package dev.growi.passwordstore.server.carddata.dao.impl.jpa.model;

import dev.growi.passwordstore.server.carddata.dao.model.AccessControlEntryDAO;
import dev.growi.passwordstore.server.userdata.dao.impl.jpa.model.JpaUser;

import javax.persistence.*;

@MappedSuperclass
public abstract class JpaAcceessControlEntry implements AccessControlEntryDAO {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long aceId;

    @ManyToOne
    private JpaAccessControlList acl;
    private byte[] secret;
    private int permission;

    public abstract JpaUser getUser();

    public abstract void setUser(JpaUser user);

    @Override
    public Long getAceId() {
        return aceId;
    }

    @Override
    public void setAceId(Long aceId) {
        this.aceId = aceId;
    }

    @Override
    public JpaAccessControlList getAcl() {
        return acl;
    }

    @Override
    public void setAcl(JpaAccessControlList acl) {
        this.acl = acl;
    }

    @Override
    public byte[] getSecret() {
        return secret;
    }

    @Override
    public void setSecret(byte[] secret) {
        this.secret = secret;
    }

    @Override
    public int getPermission() {
        return permission;
    }

    @Override
    public void setPermission(int permission) {
        this.permission = permission;
    }
}
