package dev.growi.passwordstore.server.userdata.dao.exception;

public class UserNotFoundException extends EntityNotFoundException {

    public UserNotFoundException(String query){
        super("user", query);
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }
}
