package com.ferdin.hiretrack.service;

import com.ferdin.hiretrack.dto.*;
import com.ferdin.hiretrack.entity.*;
import com.ferdin.hiretrack.exception.ResourceNotFoundException;
import com.ferdin.hiretrack.repository.ApplicationRepository;
import com.ferdin.hiretrack.repository.CompanyRepository;
import com.ferdin.hiretrack.repository.UserRepository;
import com.ferdin.hiretrack.security.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ApplicationService {

    private final ApplicationRepository applicationRepository;
    private final CompanyRepository companyRepository;
    private final UserRepository userRepository;
    private final SecurityUtils securityUtils;

    

    @Transactional
    public ApplicationResponseDTO createApplication(ApplicationRequestDTO requestDTO) {

        Long userId = securityUtils.getCurrentUserId();

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        Company company = companyRepository.findById(requestDTO.getCompanyId())
                .orElseThrow(() -> new ResourceNotFoundException("Company not found with id: " + requestDTO.getCompanyId()));

        Application application = new Application();
        application.setJobTitle(requestDTO.getJobTitle());
        application.setCompany(company);
        application.setUser(user);
        application.setStatus(requestDTO.getStatus() != null ? requestDTO.getStatus() : ApplicationStatus.SAVED);
        application.setTargetDate(requestDTO.getTargetDate());
        application.setJobUrl(requestDTO.getJobUrl());
        application.setDescription(requestDTO.getDescription());

        if (requestDTO.getNotes() != null) {
            for (String noteContent : requestDTO.getNotes()) {
                Note note = new Note();
                note.setContent(noteContent);
                note.setCreatedAt(java.time.LocalDateTime.now());
                note.setApplication(application);
                application.getNotes().add(note);
            }
        }

        if (requestDTO.getContacts() != null) {
            for (ContactRequestDTO contactDTO : requestDTO.getContacts()) {
                Contact contact = new Contact();
                contact.setName(contactDTO.getName());
                contact.setRole(contactDTO.getRole());
                contact.setEmail(contactDTO.getEmail());
                contact.setLinkedIn(contactDTO.getLinkedIn());
                contact.setApplication(application);
                application.getContacts().add(contact);
            }
        }

        Application saved = applicationRepository.save(application);
        return toResponseDTO(saved);
    }

    public PagedResponseDTO<ApplicationResponseDTO> getAllApplicationsByUser(int page, int size) {
        Long userId = securityUtils.getCurrentUserId();

        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());

        Page<Application> applicationPage = applicationRepository.findByUserId(userId, pageable);

        List<ApplicationResponseDTO> content = applicationPage.getContent()
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());

        return new PagedResponseDTO<>(
                content,
                applicationPage.getNumber(),
                applicationPage.getSize(),
                applicationPage.getTotalElements(),
                applicationPage.getTotalPages(),
                applicationPage.isLast()
        );
    }

    public ApplicationResponseDTO getApplicationById(Long id) {

        Long userId = securityUtils.getCurrentUserId();

        Application application = applicationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Application not found with id: " + id));

        if (!application.getUser().getId().equals(userId)) {
            throw new ResourceNotFoundException("Application not found with id: " + id);
        }

        return toResponseDTO(application);
    }

    @Transactional
    public ApplicationResponseDTO updateApplication(Long id, ApplicationRequestDTO requestDTO) {

        Long userId = securityUtils.getCurrentUserId();

        Application application = applicationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Application not found with id: " + id));

        if (!application.getUser().getId().equals(userId)) {
            throw new ResourceNotFoundException("Application not found with id: " + id);
        }

        Company company = companyRepository.findById(requestDTO.getCompanyId())
                .orElseThrow(() -> new ResourceNotFoundException("Company not found with id: " + requestDTO.getCompanyId()));

        application.setJobTitle(requestDTO.getJobTitle());
        application.setCompany(company);
        application.setStatus(requestDTO.getStatus() != null ? requestDTO.getStatus() : application.getStatus());
        application.setTargetDate(requestDTO.getTargetDate());
        application.setJobUrl(requestDTO.getJobUrl());
        application.setDescription(requestDTO.getDescription());

        Application saved = applicationRepository.save(application);
        return toResponseDTO(saved);
    }

    @Transactional
    public ApplicationResponseDTO updateStatus(Long id, StatusUpdateDTO statusUpdateDTO) {

        Long userId = securityUtils.getCurrentUserId();

        Application application = applicationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Application not found with id: " + id));

        if (!application.getUser().getId().equals(userId)) {
            throw new ResourceNotFoundException(
                    "Application not found with id: " + id);
        }

        application.setStatus(statusUpdateDTO.getStatus());
        Application saved = applicationRepository.save(application);
        return toResponseDTO(saved);
    }

    @Transactional
    public void deleteApplication(Long id) {

        Long userId = securityUtils.getCurrentUserId();

        Application application = applicationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Application not found with id: " + id));

        if (!application.getUser().getId().equals(userId)) {
            throw new ResourceNotFoundException("Application not found with id: " + id);
        }

        applicationRepository.delete(application);
    }

    private ApplicationResponseDTO toResponseDTO(Application application) {
        ApplicationResponseDTO dto = new ApplicationResponseDTO();
        dto.setId(application.getId());
        dto.setJobTitle(application.getJobTitle());
        dto.setCompanyName(application.getCompany().getName());
        dto.setStatus(application.getStatus());
        dto.setTargetDate(application.getTargetDate());
        dto.setJobUrl(application.getJobUrl());
        dto.setDescription(application.getDescription());
        return dto;
    }
}