package Backend.Controller;

import Backend.dto.UserDTO.LoginRequestDto;
import Backend.dto.UserDTO.RegisterRequestDto;
import Backend.service.UserServices;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
public class UserController {
    @Autowired
    private UserServices userService;

    @PostMapping("/register")
    public  String register(@RequestBody RegisterRequestDto requestDto){
        return userService.register(requestDto);
    }

    @PostMapping("/login")
    public  String Login(@RequestBody LoginRequestDto loginRequestDto){
        return   userService.Login(loginRequestDto);
    }

}
