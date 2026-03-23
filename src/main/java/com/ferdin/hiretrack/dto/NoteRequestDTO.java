package com.ferdin.hiretrack.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NoteRequestDTO {

    @NotBlank(message = "Note content cannot be blank")
    private String content;
}