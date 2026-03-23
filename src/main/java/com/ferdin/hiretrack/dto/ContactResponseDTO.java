package com.ferdin.hiretrack.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ContactResponseDTO {
    private Long id;

    private String name;

    private String role;

    private String email;

    private String linkedIn;
}
