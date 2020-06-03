package dev.growi.passwordstore.server.carddata.dao.exception;

import dev.growi.passwordstore.server.shared.dao.exception.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "No such Card")
public class CardNotFoundException extends EntityNotFoundException {

    public CardNotFoundException(String query) {
        super("card", query);
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }
}
