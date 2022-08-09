package lord.dev.dto.request;

import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PostRequest {

        private Long id;

        @NotEmpty
        @Size(min = 2, message = "Post title should have min. 2 characters")
        private String title;

        @NotEmpty
        @Size(min = 10, message = "Post description should have 10 characters")
        private String description;

        @NotEmpty
        private String content;

}
