package lord.dev.exception;

import java.util.function.Supplier;

public class UserNotFoundException extends UserAuthenticationException{

    public UserNotFoundException(String message) {
        super(message);
    }


}