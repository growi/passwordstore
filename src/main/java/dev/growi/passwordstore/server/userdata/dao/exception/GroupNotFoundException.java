package dev.growi.passwordstore.server.userdata.dao.exception;

public class GroupNotFoundException extends EntityNotFoundException {

    public GroupNotFoundException(String query){
        super("group", query);
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }
}
