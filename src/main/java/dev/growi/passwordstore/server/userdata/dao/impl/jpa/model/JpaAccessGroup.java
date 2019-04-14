package dev.growi.passwordstore.server.userdata.dao.impl.jpa.model;

import dev.growi.passwordstore.server.userdata.dao.model.GroupDAO;
import dev.growi.passwordstore.server.userdata.domain.model.IdWrapper;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Entity(name = "access_group")
public class JpaAccessGroup extends JpaPrincipal implements GroupDAO {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long groupId;
    private String groupName;

    @OneToMany(cascade=CascadeType.PERSIST, mappedBy="userMemberPK.group", fetch = FetchType.EAGER)
    private Set<JpaUserMember> members = new HashSet<>();

    @OneToMany(cascade=CascadeType.PERSIST, mappedBy="groupMemberPK.group", fetch = FetchType.EAGER)
    private Set<JpaAccessGroupMember> memberGroups = new HashSet<>();

    public JpaAccessGroup(){}

    public JpaAccessGroup(String groupName){
        this.groupName = groupName;
    }

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

    @Override
    public Collection<JpaUserMember> getMembers(){

        return this.members;
    }

    @Override
    public Collection<JpaAccessGroupMember> getMemberGroups(){

        return this.memberGroups;
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

        @Override
        public boolean equals(Object obj) {
            return obj.getClass().equals(this.getClass()) && this.getValue() != null && this.getValue().equals(((AccessGroupId)obj).getValue());
        }

        @Override
        public int hashCode(){
            return this.getValue() != null ? this.getValue().hashCode() : 0;
        }
    }
}
