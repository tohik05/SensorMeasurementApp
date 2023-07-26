package spring.rest.serversideapp.exception;

public class EntityHasAlreadyExistException extends RuntimeException {

    public EntityHasAlreadyExistException(String message) {
        super(message);
    }

}
