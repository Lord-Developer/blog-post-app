package lord.dev.exception;

public abstract class UserAuthenticationException extends RuntimeException{
    public UserAuthenticationException(String message) {
        super(message);
    }
}
