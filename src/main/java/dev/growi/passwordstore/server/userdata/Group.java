package dev.growi.passwordstore.server.userdata;

import javax.persistence.MappedSuperclass;

@MappedSuperclass
public interface Group extends Principal{

    Id<?> getGroupId();

    void setGroupId(Id<?> groupId);

    String getGroupName();

    void setGroupName(String groupName);
}
