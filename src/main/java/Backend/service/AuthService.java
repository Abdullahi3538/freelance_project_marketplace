package Backend.service;

import Backend.dto.UserDTO.AuthResponseDto;
import Backend.dto.UserDTO.LoginRequestDto;
import Backend.dto.UserDTO.RegisterRequestDto;
import Backend.entity.Auth.User;
import Backend.exception.ResourceAlreadyExistsException;
import Backend.repository.UserRepository;
import Backend.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthResponseDto register(RegisterRequestDto request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new ResourceAlreadyExistsException("Email already in use");
        }

        var user = new User();
        user.setFullName(request.getFullName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(request.getRole());

        userRepository.save(user);

        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("role", user.getRole().name());
        var jwtToken = jwtService.generateToken(extraClaims, user);

        return AuthResponseDto.builder()
                .token(jwtToken)
                .email(user.getEmail())
                .message("User registered successfully")
                .build();
    }

    public AuthResponseDto login(LoginRequestDto request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow();

        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("role", user.getRole().name());
        var jwtToken = jwtService.generateToken(extraClaims, user);

        return AuthResponseDto.builder()
                .token(jwtToken)
                .email(user.getEmail())
                .message("User logged in successfully")
                .build();
    }
}
