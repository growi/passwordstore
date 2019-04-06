package dev.growi.passwordstore.server.userdata.dao.impl.jpa.model;

import dev.growi.passwordstore.server.userdata.dao.model.GroupDAO;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity(name = "access_group_member")
public class JpaAccessGroupMember {

    @EmbeddedId
    private AccessGroupMemberPK groupUserMemberPK;

    @ManyToOne
    @JoinColumn(name="member_id", insertable=false, updatable=false)
    private JpaAccessGroup member;

    public JpaAccessGroup getMember() {
        return member;
    }

    public void setMember(GroupDAO member) {
        this.member = (JpaAccessGroup) member;
    }

    @Embeddable
    public static class AccessGroupMemberPK implements Serializable {

        @ManyToOne
        @JoinColumn(name="group_id")
        private JpaAccessGroup group;

        @ManyToOne
        @JoinColumn(name="member_id")
        private JpaAccessGroup member;

        public AccessGroupMemberPK() {}

        public AccessGroupMemberPK(JpaAccessGroup member, JpaAccessGroup group) {
            this.member = member;
            this.group = group;
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
