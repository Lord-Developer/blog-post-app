package lord.dev.exception;

public class UserAlreadyRegisteredException extends Exception{
    public UserAlreadyRegisteredException(String message) {
        super(message);
    }
}

