package dev.growi.passwordstore.server.userdata.dao.model;


import dev.growi.passwordstore.server.userdata.dao.impl.jpa.model.JpaAccessGroupMember;
import dev.growi.passwordstore.server.userdata.dao.impl.jpa.model.JpaUserMember;
import dev.growi.passwordstore.server.userdata.domain.model.IdWrapper;

import java.util.Collection;

public interface GroupDAO extends PrincipalDAO {

    IdWrapper<?> getGroupId();

    String getGroupName();

    void setGroupName(String groupName);

    Collection<JpaUserMember> getMembers();

    Collection<JpaAccessGroupMember> getMemberGroups();
}
