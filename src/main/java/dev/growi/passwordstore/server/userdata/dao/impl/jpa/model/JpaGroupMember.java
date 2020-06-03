package dev.growi.passwordstore.server.userdata.dao.impl.jpa.model;

import dev.growi.passwordstore.server.userdata.dao.model.GroupDAO;
import dev.growi.passwordstore.server.userdata.dao.model.GroupMemberDAO;
import dev.growi.passwordstore.server.userdata.dao.model.PrincipalDAO;
import dev.growi.passwordstore.server.userdata.dao.model.UserDAO;

import javax.persistence.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

@Entity(name = "group_member")
public class JpaGroupMember implements GroupMemberDAO {

    public JpaGroupMember() {
    }

    public JpaGroupMember(JpaGroupMember.GroupMemberPK pk) {
        this.groupMemberPK = pk;
    }

    @EmbeddedId
    private JpaGroupMember.GroupMemberPK groupMemberPK;

    @ManyToOne
    @JoinColumn(name = "group_id", insertable = false, updatable = false)
    protected JpaAccessGroup group;

    @ManyToOne
    @JoinColumn(name = "member_id", insertable = false, updatable = false)
    private JpaPrincipal member;

    private int permissions;
    private Instant createdStamp;
    private Instant lastUpdatedStamp;

    @Lob
    @Column(name = "secret", columnDefinition = "BLOB")
    private byte[] secret;

    @Lob
    @Column(name = "groupkey", columnDefinition = "BLOB")
    private byte[] groupKey;

    @ManyToOne
    private JpaUser createdByUser;

    @ManyToOne
    private JpaUser lastUpdatedByUser;

    @Override
    public GroupDAO getGroup() {
        return this.groupMemberPK.group;
    }

    @Override
    public void setGroup(GroupDAO group) {
        this.groupMemberPK.group = (JpaAccessGroup) group;
    }

    @Override
    public PrincipalDAO getMember() {
        return this.groupMemberPK.member;
    }

    @Override
    public void setMember(PrincipalDAO principal) {
        this.groupMemberPK.member = (JpaPrincipal) principal;
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

    @Embeddable
    public static class GroupMemberPK implements Serializable {

        @ManyToOne(cascade = CascadeType.PERSIST)
        @JoinColumn(name = "group_id")
        private JpaAccessGroup group;

        @ManyToOne(cascade = CascadeType.PERSIST)
        @JoinColumn(name = "member_id")
        private JpaPrincipal member;

        public GroupMemberPK() {
        }

        public GroupMemberPK(PrincipalDAO memberPK, GroupDAO groupPK) {
            this.member = (JpaPrincipal) memberPK;
            this.group = (JpaAccessGroup) groupPK;
        }

        public PrincipalDAO getMember() {
            return this.member;
        }

        public void setMember(PrincipalDAO member) {
            this.member = (JpaPrincipal) member;
        }

        public GroupDAO getGroup() {
            return this.group;
        }

        public void setGroup(GroupDAO group) {
            this.group = (JpaAccessGroup) group;
        }


        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            JpaGroupMember.GroupMemberPK that = (JpaGroupMember.GroupMemberPK) o;
            return Objects.equals(member, that.getMember()) &&
                    Objects.equals(group, that.getGroup());
        }

        @Override
        public int hashCode() {
            return Objects.hash(member, group);
        }
    }
}
