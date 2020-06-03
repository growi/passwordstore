package dev.growi.passwordstore.server.userdata.dao.exception;

import dev.growi.passwordstore.server.shared.dao.exception.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value= HttpStatus.NOT_FOUND, reason="No such User")
public class UserNotFoundException extends EntityNotFoundException {

    public UserNotFoundException(String query){
        super("user", query);
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }
}
