package lord.dev.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserRegisterRequest {

    @Email
    private String email;

    @NotBlank
    @NotEmpty
    @Size(min = 8, message = "Post description should have 10 characters")
    private String password;
}
