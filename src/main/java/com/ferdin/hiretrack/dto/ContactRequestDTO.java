package com.ferdin.hiretrack.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ContactRequestDTO {

    @NotBlank(message = "Contact name cannot be blank")
    private String name;

    private String role;

    private String email;

    private String linkedIn;
}