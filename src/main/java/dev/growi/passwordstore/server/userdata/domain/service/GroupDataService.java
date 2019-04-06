package dev.growi.passwordstore.server.userdata.domain.service;

import dev.growi.passwordstore.server.userdata.dao.provider.GroupDataProvider;
import dev.growi.passwordstore.server.userdata.dao.exception.GroupNotFoundException;
import dev.growi.passwordstore.server.userdata.domain.model.Group;
import dev.growi.passwordstore.server.userdata.domain.model.IdWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GroupDataService {

    @Autowired
    GroupDataProvider groupDataProvider;

    public Group getById(IdWrapper<?> id) throws GroupNotFoundException {

        return new Group(groupDataProvider.findByGroupId(id));
    }

    public Group getByName(String groupName) throws GroupNotFoundException{

         return new Group(groupDataProvider.findByGroupName(groupName));
    }

}
