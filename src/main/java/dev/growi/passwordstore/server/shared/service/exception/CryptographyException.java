package dev.growi.passwordstore.server.shared.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value= HttpStatus.INTERNAL_SERVER_ERROR, reason="Secrity Violation")
public class CryptographyException extends Exception {

    public CryptographyException(Throwable t){
        super(t);
    }
}
