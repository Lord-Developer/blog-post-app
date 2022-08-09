package lord.dev.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor

public class BlogApiException extends RuntimeException{

    private HttpStatus status;
    private String massage;

}
