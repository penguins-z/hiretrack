package com.ferdin.hiretrack.dto;

import com.ferdin.hiretrack.entity.ApplicationStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApplicationResponseDTO {

    private Long id;
    private String jobTitle;
    private String companyName;
    private ApplicationStatus status;
    private LocalDate targetDate;
    private String jobUrl;
    private String description;
}