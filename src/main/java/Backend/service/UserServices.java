package Backend.service;

import Backend.dto.UserDTO.LoginRequestDto;
import Backend.dto.UserDTO.RegisterRequestDto;
import Backend.dto.UserDTO.UserResponseDto;
import Backend.entity.Auth.User;
import Backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServices {
    @Autowired
    private UserRepository userRepository;

    public  String register(RegisterRequestDto Request){
        if (userRepository.findByEmail(Request.getEmail()).isPresent()){
            return  "Email already exists";
        }
        User user = new User();
        user.setFullName(Request.getFullName());
        user.setEmail(Request.getEmail());
        user.setPassword(Request.getPassword());
        user.setRole(Request.getRole());
        userRepository.save(user);
        return "Registration successful";
    }

    public String Login(LoginRequestDto loginRequestDto){
        User user = userRepository.findByEmail(loginRequestDto.getEmail())
                .orElse(null);
        if (user==null){
            return "User not found";
        }
        if (!user.getPassword().equals(loginRequestDto.getPassword())) {
            return "Incorrect password";
        }
        return "Login successful";

    }

    public List<UserResponseDto> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    private UserResponseDto mapToResponse(User user) {
        UserResponseDto response = new UserResponseDto();
        response.setId(user.getId());
        response.setFullName(user.getFullName());
        response.setEmail(user.getEmail());
        response.setRole(user.getRole());
        response.setCreatedAt(user.getCreatedAt());
        return response;
    }
}
