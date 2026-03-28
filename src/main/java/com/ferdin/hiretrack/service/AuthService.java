package com.ferdin.hiretrack.service;

import com.ferdin.hiretrack.dto.AuthResponseDTO;
import com.ferdin.hiretrack.dto.LoginRequestDTO;
import com.ferdin.hiretrack.dto.RegisterRequestDTO;
import com.ferdin.hiretrack.entity.User;
import com.ferdin.hiretrack.exception.DuplicateResourceException;
import com.ferdin.hiretrack.exception.ResourceNotFoundException;
import com.ferdin.hiretrack.repository.UserRepository;
import com.ferdin.hiretrack.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    public AuthResponseDTO register(RegisterRequestDTO requestDTO) {
        log.info("Received register request for email : {}", requestDTO.getEmail());
        if (userRepository.findByEmail(requestDTO.getEmail()).isPresent()) {
            log.warn("User already exists for email : {}", requestDTO.getEmail());
            throw new DuplicateResourceException("Email already registered: " + requestDTO.getEmail());
        }


        User user = new User();
        user.setName(requestDTO.getName());
        user.setEmail(requestDTO.getEmail());
        user.setPassword(passwordEncoder.encode(requestDTO.getPassword()));

        User saved = userRepository.save(user);

        String token = jwtUtil.generateToken(saved.getEmail(), saved.getId());

        log.info("User registered with email : {}", saved.getEmail());

        return new AuthResponseDTO(token, saved.getId(), saved.getEmail());
    }

    public AuthResponseDTO login(LoginRequestDTO requestDTO) {

        log.info("Received login request for email : {}", requestDTO.getEmail());
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        requestDTO.getEmail(),
                        requestDTO.getPassword()
                )
        );

        User user = userRepository.findByEmail(requestDTO.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User not found with email: " + requestDTO.getEmail()));

        String token = jwtUtil.generateToken(user.getEmail(), user.getId());

        return new AuthResponseDTO(token, user.getId(), user.getEmail());
    }
}