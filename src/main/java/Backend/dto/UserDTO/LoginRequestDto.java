package Backend.dto.UserDTO;

import lombok.Data;

@Data
public class LoginRequestDto {
    private String email;
    private String password;
}
