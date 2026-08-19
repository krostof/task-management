package com.example.task_managemnt_system.security;

import com.example.api.model.AuthResponse;
import com.example.api.model.LoginRequest;
import com.example.api.model.RegisterRequest;
import com.example.domain.dto.UserDto;
import com.example.domain.service.UserService;
import com.example.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final PasswordEncoder passwordEncoder;
    private final UserService  userService;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthResponse register(RegisterRequest registerRequest) {

        String password = registerRequest.getPassword();
        String encodedPassword = passwordEncoder.encode(password);

        UserDto userDto = new UserDto(
                registerRequest.getName(),
                registerRequest.getSurname(),
                encodedPassword,
                registerRequest.getEmail()
        );

        userService.save(userDto);

        AuthResponse authResponse = new AuthResponse();
        authResponse.setToken(jwtService.generateJwtToken(registerRequest.getEmail()));
        return authResponse;
    }

    public AuthResponse login(LoginRequest loginRequest) {
        Authentication authenticationRequest =
                UsernamePasswordAuthenticationToken.unauthenticated(loginRequest.getEmail(), loginRequest.getPassword());
        Authentication authentication = authenticationManager.authenticate(authenticationRequest);
        AuthResponse authResponse = new AuthResponse();
        authResponse.setToken(jwtService.generateJwtToken(authentication.getName()));
        return authResponse;
    }

}
