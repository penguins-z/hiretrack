package com.ferdin.hiretrack.controller;

import com.ferdin.hiretrack.dto.AuthResponseDTO;
import com.ferdin.hiretrack.dto.LoginRequestDTO;
import com.ferdin.hiretrack.dto.RegisterRequestDTO;
import com.ferdin.hiretrack.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponseDTO> register(
            @Valid @RequestBody RegisterRequestDTO requestDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.register(requestDTO));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(
            @Valid @RequestBody LoginRequestDTO requestDTO) {
        return ResponseEntity.ok(authService.login(requestDTO));
    }
}