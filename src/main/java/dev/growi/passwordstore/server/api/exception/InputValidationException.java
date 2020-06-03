package dev.growi.passwordstore.server.api.exception;

public class InoutValidationException extends RuntimeException {

    InoutValidationException(String message){
        super(message);
    }
}
