package Backend.dto.UserDTO;

import Backend.Enmu.Role;
import lombok.Data;

@Data
public class RegisterRequestDto {
    private String fullName;
    private String email;
    private Role role;
    private String password;
}
