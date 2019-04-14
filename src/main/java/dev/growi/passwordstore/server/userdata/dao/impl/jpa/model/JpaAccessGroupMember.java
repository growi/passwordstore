package dev.growi.passwordstore.server.userdata.dao.impl.jpa.model;

import dev.growi.passwordstore.server.userdata.dao.model.GroupDAO;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity(name = "access_group_member")
public class JpaAccessGroupMember extends JpaGroupMember {

    @EmbeddedId
    private AccessGroupMemberPK groupMemberPK;

    @ManyToOne
    @JoinColumn(name="member_id", insertable=false, updatable=false)
    private JpaAccessGroup member;

    public JpaAccessGroupMember(){}

    public  JpaAccessGroupMember(AccessGroupMemberPK accessGroupMemberPK){
        this.groupMemberPK = accessGroupMemberPK;
    }

    public JpaAccessGroup getMember() {
        return this.groupMemberPK.member;
    }

    public void setMember(GroupDAO member) {
        this.groupMemberPK.member = (JpaAccessGroup) member;
    }

    @Embeddable
    public static class AccessGroupMemberPK implements Serializable {

        @ManyToOne(cascade = CascadeType.PERSIST)
        @JoinColumn(name="group_id")
        private JpaAccessGroup group;

        @ManyToOne(cascade = CascadeType.PERSIST)
        @JoinColumn(name="member_id")
        private JpaAccessGroup member;

        public AccessGroupMemberPK() {}

        public AccessGroupMemberPK(GroupDAO member, GroupDAO group) {
            this.member = (JpaAccessGroup) member;
            this.group = (JpaAccessGroup) group;
        }

        public GroupDAO getMember() {
            return member;
        }

        public void setMember(GroupDAO member) {
            this.member = (JpaAccessGroup) member;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            AccessGroupMemberPK that = (AccessGroupMemberPK) o;
            return Objects.equals(member, that.member) &&
                    Objects.equals(group, that.group);
        }

        @Override
        public int hashCode() {
            return Objects.hash(member, group);
        }
    }
}
