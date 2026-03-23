package com.ferdin.hiretrack.service;

import com.ferdin.hiretrack.dto.ContactRequestDTO;
import com.ferdin.hiretrack.dto.ContactResponseDTO;
import com.ferdin.hiretrack.entity.Application;
import com.ferdin.hiretrack.entity.Contact;
import com.ferdin.hiretrack.exception.ResourceNotFoundException;
import com.ferdin.hiretrack.repository.ApplicationRepository;
import com.ferdin.hiretrack.repository.ContactRepository;
import com.ferdin.hiretrack.security.SecurityUtils;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ContactService {

    private final ContactRepository contactRepository;
    private final ApplicationRepository applicationRepository;
    private final SecurityUtils securityUtils;
    @Transactional
    public ContactResponseDTO addContact(Long applicationId, ContactRequestDTO contactRequestDTO) {
        Long userId = securityUtils.getCurrentUserId();
        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Application not found with id: " + applicationId));

        if (!application.getUser().getId().equals(userId)) {
            throw new ResourceNotFoundException(
                    "Application not found with id: " + applicationId);
        }

        Contact contact = new Contact();
        contact.setName(contactRequestDTO.getName());
        contact.setEmail(contactRequestDTO.getEmail());
        contact.setRole(contactRequestDTO.getRole());
        contact.setLinkedIn(contactRequestDTO.getLinkedIn());
        contact.setApplication(application);

        Contact created = contactRepository.save(contact);

        return toResponseDTO(created);
    }

    public List<ContactResponseDTO> getContactsByApplicationId(Long applicationId) {
        Long userId = securityUtils.getCurrentUserId();
        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Application not found with id: " + applicationId));

        if (!application.getUser().getId().equals(userId)) {
            throw new ResourceNotFoundException(
                    "Application not found with id: " + applicationId);
        }

        return contactRepository.findByApplicationId(applicationId)
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteContact(Long applicationId, Long contactId) {
        Long userId = securityUtils.getCurrentUserId();
        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Application not found with id: " + applicationId));

        if (!application.getUser().getId().equals(userId)) {
            throw new ResourceNotFoundException(
                    "Application not found with id: " + applicationId);
        }

        Contact contact = contactRepository.findById(contactId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Contact with id: " + contactId + " not found"));

        if(!contact.getApplication().getId().equals(applicationId)) {
            throw new ResourceNotFoundException(
                    "Application not found with id: " + applicationId);
        }

        contactRepository.deleteById(contactId);
    }

    private ContactResponseDTO toResponseDTO(Contact contact){
        ContactResponseDTO responseDTO = new ContactResponseDTO();
        responseDTO.setId(contact.getId());
        responseDTO.setName(contact.getName());
        responseDTO.setEmail(contact.getEmail());
        responseDTO.setRole(contact.getRole());
        responseDTO.setLinkedIn(contact.getLinkedIn());

        return responseDTO;
    }
}
