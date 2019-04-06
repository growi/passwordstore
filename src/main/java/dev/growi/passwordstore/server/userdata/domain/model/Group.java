package dev.growi.passwordstore.server.userdata.domain.model;

import dev.growi.passwordstore.server.userdata.dao.model.GroupDAO;

public class Group extends Principal {

    private GroupDAO groupDAO;

    public Group(GroupDAO groupDAO) {
        this.groupDAO = groupDAO;
    }

    public void addMember(Principal principal){

    }

}
