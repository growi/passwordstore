package dev.growi.passwordstore.server.api.exception;

public class InputValidationException extends RuntimeException {

    public InputValidationException(String message){
        super(message);
    }
}
