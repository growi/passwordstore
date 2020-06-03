package dev.growi.passwordstore.server.userdata.dao.exception;

import dev.growi.passwordstore.server.shared.dao.exception.EntityNotFoundException;

public class GroupMemberNotFoundException extends EntityNotFoundException {

    public GroupMemberNotFoundException(String query){
        super("group member", query);
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }
}
