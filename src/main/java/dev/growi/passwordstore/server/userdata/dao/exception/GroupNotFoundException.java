package dev.growi.passwordstore.server.userdata.dao.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value= HttpStatus.NOT_FOUND, reason="No such Group")
public class GroupNotFoundException extends EntityNotFoundException {

    public GroupNotFoundException(String query){
        super("group", query);
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }
}
