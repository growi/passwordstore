package dev.growi.passwordstore.server.userdata.dao.provider;

import dev.growi.passwordstore.server.userdata.dao.model.GroupDAO;
import dev.growi.passwordstore.server.userdata.dao.exception.GroupNotFoundException;
import dev.growi.passwordstore.server.userdata.domain.model.IdWrapper;

public interface GroupDataProvider {

    GroupDAO findByGroupId(IdWrapper<?> groupId) throws GroupNotFoundException;

    GroupDAO findByGroupName(String groupName) throws GroupNotFoundException;

}
