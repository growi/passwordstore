package dev.growi.passwordstore.server.userdata.jpa.dao;

import dev.growi.passwordstore.server.userdata.Group;
import dev.growi.passwordstore.server.userdata.GroupMember;
import dev.growi.passwordstore.server.userdata.Principal;
import dev.growi.passwordstore.server.userdata.User;

import javax.crypto.EncryptedPrivateKeyInfo;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import java.time.Instant;

@MappedSuperclass
public class GroupMemberDAO implements GroupMember {

    @ManyToOne
    @JoinColumn(name="group_id", insertable=false, updatable=false)
    protected AccessGroupDAO group;

    private int permissions;
    private byte[] secret;
    private byte[] groupKey;
    private User createdByUser;
    private User lastUpdatedByUser;
    private Instant createdStamp;
    private Instant lastUpdatedStamp;

    @Override
    public Group getGroup() {
        return this.group;
    }

    @Override
    public void setGroup(Group group) {
        this.group = (AccessGroupDAO) group;
    }

    @Override
    public Principal getMember() {
        return null;
    }

    @Override
    public void setPrincipal(Principal principal) {

    }

    @Override
    public int getPermissions() {
        return this.permissions;
    }

    @Override
    public void setPermissions(int permissions) {
        this.permissions = permissions;
    }

    @Override
    public byte[] getSecret() {
        return this.secret;
    }

    @Override
    public void setSecret(byte[] secret) {
        this.secret = secret;
    }

    @Override
    public EncryptedPrivateKeyInfo getGroupKey() {
        return null;
    }

    @Override
    public void setGroupKey(EncryptedPrivateKeyInfo groupKey) {

    }

    @Override
    public User getCreatedByUser() {
        return this.createdByUser;
    }

    @Override
    public void setCreatedByUser(User createdByUser) {
        this.createdByUser = createdByUser;
    }

    @Override
    public Instant getCreatedStamp() {
        return this.createdStamp;
    }

    @Override
    public void setCreatedStamp(Instant createdStamp) {
        this.createdStamp = createdStamp;
    }

    @Override
    public User getUpdatedByUser() {
        return this.lastUpdatedByUser;
    }

    @Override
    public void setUpdatedByUsr(User updatedByuser) {
        this.lastUpdatedByUser = updatedByuser;
    }

    @Override
    public Instant getLastUpdatedStamp() {
        return this.lastUpdatedStamp;
    }

    @Override
    public void setLastUpdatedStamp(Instant lastUpdatedStamp) {
        this.lastUpdatedStamp = lastUpdatedStamp;
    }
}
