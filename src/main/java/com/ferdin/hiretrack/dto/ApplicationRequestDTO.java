package com.ferdin.hiretrack.dto;

import com.ferdin.hiretrack.entity.ApplicationStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApplicationRequestDTO {

    @NotBlank(message = "Job title cannot be blank")
    private String jobTitle;

    @NotNull(message = "Company ID cannot be null")
    private Long companyId;

    private ApplicationStatus status;

    private LocalDate targetDate;

    private String jobUrl;

    private String description;

    private List<String> notes = new ArrayList<>();

    private List<ContactRequestDTO> contacts = new ArrayList<>();
}