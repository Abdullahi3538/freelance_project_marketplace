package Backend.controller;

import Backend.dto.UserDTO.LoginRequestDto;
import Backend.dto.UserDTO.RegisterRequestDto;
import Backend.dto.UserDTO.UserResponseDto;
import Backend.service.UserServices;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
@CrossOrigin("http://localhost:5173/")
public class UserController {
    @Autowired
    private UserServices userService;

    @PostMapping("/register")
    public  String register(@Valid @RequestBody RegisterRequestDto requestDto){
        return userService.register(requestDto);
    }

    @PostMapping("/login")
    public  String Login(@Valid @RequestBody LoginRequestDto loginRequestDto){
        return   userService.Login(loginRequestDto);
    }

    @GetMapping("/users")
    public List<UserResponseDto> getAllUsers() {
        return userService.getAllUsers();
    }

}
