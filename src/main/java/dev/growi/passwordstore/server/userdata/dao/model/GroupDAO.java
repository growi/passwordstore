package dev.growi.passwordstore.server.userdata.dao.model;


import dev.growi.passwordstore.server.userdata.dao.impl.jpa.model.JpaAccessGroupMember;
import dev.growi.passwordstore.server.userdata.dao.impl.jpa.model.JpaUserMember;
import dev.growi.passwordstore.server.userdata.domain.model.IdWrapper;

import java.util.Collection;
import java.util.Set;

public interface GroupDAO extends PrincipalDAO {

    Long getId();

    String getGroupName();

    void setGroupName(String groupName);

    <T extends GroupMemberDAO> Set<T> getMembers();

}
