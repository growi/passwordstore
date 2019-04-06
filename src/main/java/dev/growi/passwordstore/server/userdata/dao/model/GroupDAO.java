package dev.growi.passwordstore.server.userdata.dao.model;


import dev.growi.passwordstore.server.userdata.domain.model.IdWrapper;

import javax.persistence.MappedSuperclass;

@MappedSuperclass
public interface GroupDAO extends PrincipalDAO {

    IdWrapper<?> getGroupId();

    String getGroupName();

    void setGroupName(String groupName);
}
