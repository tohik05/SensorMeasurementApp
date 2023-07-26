package spring.rest.serversideapp.exception;

public class EntityValidationException extends RuntimeException{
    public EntityValidationException(String message) {
        super(message);
    }
}
