package dev.growi.passwordstore.server.userdata.domain.exception;

public class InvalidPasswordException extends Exception {

    public InvalidPasswordException(){
        super("Wrong Password.");
    }
}
