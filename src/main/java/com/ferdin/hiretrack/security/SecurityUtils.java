package com.ferdin.hiretrack.security;

import com.ferdin.hiretrack.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import com.ferdin.hiretrack.exception.ResourceNotFoundException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SecurityUtils {

    private final UserRepository userRepository;

    public Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder
                .getContext()
                .getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new ResourceNotFoundException("No authenticated user found");
        }

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String email =  userDetails.getUsername();
        return userRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("User not found")).getId();
    }
}