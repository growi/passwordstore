package dev.growi.passwordstore.server.userdata.domain.service;

import dev.growi.passwordstore.server.userdata.dao.provider.GroupDataProvider;
import dev.growi.passwordstore.server.userdata.dao.provider.UserDataProvider;
import dev.growi.passwordstore.server.userdata.domain.model.Group;
import dev.growi.passwordstore.server.userdata.domain.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CollectionLoaderService {

    @Autowired
    private UserDataProvider userDataProvider;

    @Autowired
    private GroupDataProvider groupDataProvider;

    public List<User> getAllUsers() {
        return userDataProvider.findAll().stream().map(user -> new User(user)).collect(Collectors.toList());
    }

    public List<Group> getAllGroups() {
        return groupDataProvider.findAll().stream().map(group -> new Group(group)).collect(Collectors.toList());
    }

}
