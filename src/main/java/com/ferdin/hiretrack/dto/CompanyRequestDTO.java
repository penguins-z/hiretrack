package com.ferdin.hiretrack.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CompanyRequestDTO {

    @NotBlank(message = "Company name cannot be blank")
    @Size(min = 2, max = 100, message = "Company name must be between 2 and 100 characters")
    private String name;

    @NotBlank(message = "Industry cannot be blank")
    private String industry;

    private String website;

    private String email;

    private String location;
}