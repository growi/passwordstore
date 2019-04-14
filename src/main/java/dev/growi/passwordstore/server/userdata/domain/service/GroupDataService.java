package dev.growi.passwordstore.server.userdata.domain.service;

import dev.growi.passwordstore.server.userdata.dao.exception.CryptographyException;
import dev.growi.passwordstore.server.userdata.dao.exception.UserNotFoundException;
import dev.growi.passwordstore.server.userdata.dao.model.GroupDAO;
import dev.growi.passwordstore.server.userdata.dao.provider.GroupDataProvider;
import dev.growi.passwordstore.server.userdata.dao.exception.GroupNotFoundException;
import dev.growi.passwordstore.server.userdata.domain.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class GroupDataService {

    private Map<IdWrapper<?>, Group> beanCache = new HashMap<>();

    @Autowired
    GroupDataProvider groupDataProvider;

    @Autowired
    CryptographyService cryptographyService;

    public Group get(GroupDAO group) {
        if (!beanCache.containsKey(group.getGroupId())) {
            beanCache.put(group.getGroupId(), new Group(group));
        }
        return beanCache.get(group.getGroupId());
    }

    public List<Group> getAll() {
        return groupDataProvider.findAll().stream().map(group -> get(group)).collect(Collectors.toList());
    }

    public Group getById(IdWrapper<?> id) throws GroupNotFoundException {

        return get(groupDataProvider.findByGroupId(id));
    }

    public Group getByName(String groupName) throws GroupNotFoundException {

        return get(groupDataProvider.findByGroupName(groupName));
    }

    public Group createGroup(String groupName, UserDetails activeUser) throws UserNotFoundException, CryptographyException {

        return get(groupDataProvider.createGroup(groupName, activeUser));
    }
}
