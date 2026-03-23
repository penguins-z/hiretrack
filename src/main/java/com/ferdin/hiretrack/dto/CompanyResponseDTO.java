package com.ferdin.hiretrack.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CompanyResponseDTO {

    private Long id;
    private String name;
    private String industry;
    private String website;
    private String email;
    private String location;
}