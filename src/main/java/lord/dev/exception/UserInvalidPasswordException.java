package lord.dev.exception;

public class UserInvalidPasswordException extends UserAuthenticationException {
    public UserInvalidPasswordException(String message) {
        super(message);
    }
}
