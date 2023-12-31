package urfu.test_kurs.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private Long id;

    @NotEmpty
    private String name;

    @NotEmpty(message = "Username should not be empty")
    private String username;

    @NotEmpty(message = "Password should be empty")
    private String password;
}
