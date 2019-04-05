package dev.growi.passwordstore.server.userdata.jpa.dao;

import dev.growi.passwordstore.server.userdata.Group;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity(name = "access_group_member")
public class AccessGroupMemberDAO {

    @EmbeddedId
    private AccessGroupMemberPK groupUserMemberPK;

    @ManyToOne
    @JoinColumn(name="member_id", insertable=false, updatable=false)
    private AccessGroupDAO member;

    public AccessGroupDAO getMember() {
        return member;
    }

    public void setMember(Group member) {
        this.member = (AccessGroupDAO) member;
    }

    @Embeddable
    public static class AccessGroupMemberPK implements Serializable {

        @ManyToOne
        @JoinColumn(name="group_id")
        private AccessGroupDAO group;

        @ManyToOne
        @JoinColumn(name="member_id")
        private AccessGroupDAO member;

        public AccessGroupMemberPK() {}

        public AccessGroupMemberPK(AccessGroupDAO member, AccessGroupDAO group) {
            this.member = member;
            this.group = group;
        }

        public Group getMember() {
            return member;
        }

        public void setMember(Group member) {
            this.member = (AccessGroupDAO) member;
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
