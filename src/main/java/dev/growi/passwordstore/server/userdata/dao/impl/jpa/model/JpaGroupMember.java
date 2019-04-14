package dev.growi.passwordstore.server.userdata.dao.impl.jpa.model;

import dev.growi.passwordstore.server.userdata.dao.model.GroupDAO;
import dev.growi.passwordstore.server.userdata.dao.model.GroupMemberDAO;
import dev.growi.passwordstore.server.userdata.dao.model.PrincipalDAO;
import dev.growi.passwordstore.server.userdata.dao.model.UserDAO;

import javax.persistence.*;
import java.time.Instant;

@MappedSuperclass
public class JpaGroupMember implements GroupMemberDAO {

    @ManyToOne
    @JoinColumn(name="group_id", insertable=false, updatable=false)
    protected JpaAccessGroup group;

    private int permissions;
    private Instant createdStamp;
    private Instant lastUpdatedStamp;

    @Lob
    @Column(name = "secret", columnDefinition="BLOB")
    private byte[] secret;

    @Lob
    @Column(name = "groupkey", columnDefinition="BLOB")
    private byte[] groupKey;

    @ManyToOne
    private JpaUser createdByUser;

    @ManyToOne
    private JpaUser lastUpdatedByUser;

    @Override
    public GroupDAO getGroup() {
        return this.group;
    }

    @Override
    public void setGroup(GroupDAO group) {
        this.group = (JpaAccessGroup) group;
    }

    @Override
    public PrincipalDAO getMember() {
        return null;
    }

    @Override
    public void setPrincipal(PrincipalDAO principal) {

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
    public byte[] getGroupKey() {
        return this.groupKey;
    }

    @Override
    public void setGroupKey(byte[] groupKey) {
        this.groupKey = groupKey;
    }

    @Override
    public UserDAO getCreatedByUser() {
        return this.createdByUser;
    }

    @Override
    public void setCreatedByUser(UserDAO createdByUser) {
        this.createdByUser = (JpaUser) createdByUser;
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
    public UserDAO getLastUpdatedByUser() {
        return this.lastUpdatedByUser;
    }

    @Override
    public void setLastUpdatedByUser(UserDAO updatedByuser) {
        this.lastUpdatedByUser = (JpaUser) updatedByuser;
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
