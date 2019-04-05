package dev.growi.passwordstore.server.userdata.jpa.dao;

import dev.growi.passwordstore.server.userdata.Group;
import dev.growi.passwordstore.server.userdata.Id;

import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import java.io.Serializable;

@Entity(name = "access_group")
public class AccessGroupDAO extends PrincipalDAO implements Group {

    @EmbeddedId
    private AccessGroupId groupId;
    private String groupName;

    @Override
    public Id<Long> getGroupId() {
        return this.groupId;
    }

    @Override
    public void setGroupId(Id<?> groupId) {
        this.groupId = (AccessGroupId) groupId;
    }

    @Override
    public String getGroupName() {
        return this.groupName;
    }

    @Override
    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    @Embeddable
    public static class AccessGroupId implements Id<Long>, Serializable {

        private Long groupId;

        public AccessGroupId() {}

        public AccessGroupId(Long groupId){
            this.groupId = groupId;
        }

        @Override
        public Class<Long> getIdClass() {
            return Long.class;
        }

        @Override
        public Long getValue() {
            return this.groupId;
        }

        @Override
        public void setValue(Long groupId) {
            this.groupId = groupId;
        }
    }
}
