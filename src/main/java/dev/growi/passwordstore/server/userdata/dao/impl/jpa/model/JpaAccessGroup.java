package dev.growi.passwordstore.server.userdata.dao.impl.jpa.model;

import dev.growi.passwordstore.server.userdata.dao.model.GroupDAO;
import dev.growi.passwordstore.server.userdata.dao.model.GroupMemberDAO;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity(name = "access_group")
public class JpaAccessGroup extends JpaPrincipal implements GroupDAO {

    private String groupName;

    @OneToMany(cascade=CascadeType.PERSIST, mappedBy="groupMemberPK.group", fetch = FetchType.EAGER)
    private Set<JpaGroupMember> members = new HashSet<>();

    public JpaAccessGroup(){}

    public JpaAccessGroup(String groupName){
        this.groupName = groupName;
    }

    @Override
    public <T extends GroupMemberDAO> Set<T>  getMembers(){
        return (Set<T>) this.members;
    }

    @Override
    public String getGroupName() {
        return this.groupName;
    }

    @Override
    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }
}
