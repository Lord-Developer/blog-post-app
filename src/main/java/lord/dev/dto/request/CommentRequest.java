package lord.dev.dto.request;

import lombok.*;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class CommentRequest {

    @NotEmpty(message = "Comment body should not be null or empty")
    private String text;



}

