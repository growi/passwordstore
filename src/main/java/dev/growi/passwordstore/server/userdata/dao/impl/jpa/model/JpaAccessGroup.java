package dev.growi.passwordstore.server.userdata.dao.impl.jpa.model;

import dev.growi.passwordstore.server.userdata.dao.model.GroupDAO;
import dev.growi.passwordstore.server.userdata.domain.model.IdWrapper;

import javax.persistence.*;
import java.io.Serializable;

@Entity(name = "access_group")
public class JpaAccessGroup extends JpaPrincipal implements GroupDAO {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long groupId;
    private String groupName;

    @Override
    public IdWrapper<Long> getGroupId() {
        return new AccessGroupId();
    }

    @Override
    public String getGroupName() {
        return this.groupName;
    }

    @Override
    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public class AccessGroupId implements IdWrapper<Long>, Serializable {

        public AccessGroupId() {}

        @Override
        public Class<Long> getIdClass() {
            return Long.class;
        }

        @Override
        public Long getValue() {
            return groupId;
        }

        @Override
        public void setValue(Long newGroupId) {
            groupId = newGroupId;
        }
    }
}
