package dev.growi.passwordstore.server.userdata.dao.impl.jpa.model;

import dev.growi.passwordstore.server.userdata.dao.model.GroupDAO;
import dev.growi.passwordstore.server.userdata.dao.model.UserDAO;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity(name = "user_member")
public class JpaUserMember {

    @EmbeddedId
    private UserMemberPK groupUserMemberPK;

    @ManyToOne
    @JoinColumn(name="user_id", insertable=false, updatable=false)
    private JpaUser member;

    public UserDAO getMember() {
        return member;
    }

    public void setMember(UserDAO member) {
        this.member = (JpaUser) member;
    }

    @Embeddable
    public static class UserMemberPK implements Serializable {

        @ManyToOne
        @JoinColumn(name="group_id")
        private JpaAccessGroup group;

        @ManyToOne
        @JoinColumn(name="user_id")
        private JpaUser member;

        public UserMemberPK() {}

        public UserMemberPK(UserDAO user, GroupDAO group) {
            this.member = (JpaUser) user;
            this.group = (JpaAccessGroup) group;
        }

        public UserDAO getMember() {
            return member;
        }

        public void setMember(UserDAO member) {
            this.member = (JpaUser) member;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            UserMemberPK that = (UserMemberPK) o;
            return Objects.equals(member, that.member) &&
                    Objects.equals(group, that.group);
        }

        @Override
        public int hashCode() {
            return Objects.hash(member, group);
        }
    }
}
