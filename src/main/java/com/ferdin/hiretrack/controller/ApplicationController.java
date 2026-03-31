package com.ferdin.hiretrack.controller;

import com.ferdin.hiretrack.dto.ApplicationRequestDTO;
import com.ferdin.hiretrack.dto.ApplicationResponseDTO;
import com.ferdin.hiretrack.dto.PagedResponseDTO;
import com.ferdin.hiretrack.dto.StatusUpdateDTO;
import com.ferdin.hiretrack.entity.ApplicationStatus;
import com.ferdin.hiretrack.service.ApplicationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/applications")
@RequiredArgsConstructor
public class ApplicationController {

    private final ApplicationService applicationService;

    @PostMapping
    public ResponseEntity<ApplicationResponseDTO> createApplication(
            @Valid @RequestBody ApplicationRequestDTO requestDTO) {
        ApplicationResponseDTO created = applicationService.createApplication(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping
    public ResponseEntity<PagedResponseDTO<ApplicationResponseDTO>> getAllApplications(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) ApplicationStatus status) {
        return ResponseEntity.ok(applicationService.getAllApplicationsByUser(page, size, status));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApplicationResponseDTO> getApplicationById(
            @PathVariable Long id) {
        return ResponseEntity.ok(applicationService.getApplicationById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApplicationResponseDTO> updateApplication(
            @PathVariable Long id,
            @Valid @RequestBody ApplicationRequestDTO requestDTO) {
        return ResponseEntity.ok(applicationService.updateApplication(id, requestDTO));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<ApplicationResponseDTO> updateStatus(
            @PathVariable Long id,
            @Valid @RequestBody StatusUpdateDTO statusUpdateDTO) {
        return ResponseEntity.ok(applicationService.updateStatus(id, statusUpdateDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteApplication(
            @PathVariable Long id) {
        applicationService.deleteApplication(id);
        return ResponseEntity.noContent().build();
    }
}