package dev.growi.passwordstore.server.api.exception;

public class InputParsingException extends RuntimeException {

    public InputParsingException(String message){
        super(message);
    }

    public InputParsingException(Throwable t){
        super(t);
    }
}
