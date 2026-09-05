package com.example.task_managemnt_system.controller;

import com.example.api.AuthApi;
import com.example.api.model.AuthResponse;
import com.example.api.model.LoginRequest;
import com.example.api.model.RegisterRequest;
import com.example.task_managemnt_system.security.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController implements AuthApi {

    private final AuthService authService;

    @Override
    public ResponseEntity<AuthResponse> registerUser(RegisterRequest registerRequest) {
        AuthResponse register = authService.register(registerRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(register);
    }

    @Override
    public ResponseEntity<AuthResponse> loginUser(LoginRequest loginRequest) {
        AuthResponse login = authService.login(loginRequest);
        return ResponseEntity.ok(login);
    }
}
