package com.ferdin.hiretrack.dto;

import com.ferdin.hiretrack.entity.ApplicationStatus;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StatusUpdateDTO {

    @NotNull(message = "Status cannot be null")
    private ApplicationStatus status;
}