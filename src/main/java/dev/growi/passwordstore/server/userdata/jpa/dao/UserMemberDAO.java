package dev.growi.passwordstore.server.userdata.jpa.dao;

import dev.growi.passwordstore.server.userdata.Group;
import dev.growi.passwordstore.server.userdata.User;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity(name = "user_member")
public class UserMemberDAO {

    @EmbeddedId
    private UserMemberPK groupUserMemberPK;

    @ManyToOne
    @JoinColumn(name="user_id", insertable=false, updatable=false)
    private UserDAO member;

    public User getMember() {
        return member;
    }

    public void setMember(User member) {
        this.member = (UserDAO) member;
    }

    @Embeddable
    public static class UserMemberPK implements Serializable {

        @ManyToOne
        @JoinColumn(name="group_id")
        private AccessGroupDAO group;

        @ManyToOne
        @JoinColumn(name="user_id")
        private UserDAO member;

        public UserMemberPK() {}

        public UserMemberPK(User user, Group group) {
            this.member = (UserDAO) user;
            this.group = (AccessGroupDAO) group;
        }

        public User getMember() {
            return member;
        }

        public void setMember(User member) {
            this.member = (UserDAO) member;
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
