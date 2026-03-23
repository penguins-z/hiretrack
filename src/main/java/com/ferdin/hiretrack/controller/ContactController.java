package com.ferdin.hiretrack.controller;

import com.ferdin.hiretrack.dto.ContactRequestDTO;
import com.ferdin.hiretrack.dto.ContactResponseDTO;
import com.ferdin.hiretrack.service.ContactService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/applications/{applicationId}/contacts")
@RequiredArgsConstructor
public class ContactController {

    private final ContactService contactService;

    @PostMapping
    public ResponseEntity<ContactResponseDTO> createContact(
            @PathVariable Long applicationId,
            @RequestBody ContactRequestDTO contactRequestDTO){

        ContactResponseDTO contactResponseDTO = contactService.addContact(applicationId, contactRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(contactResponseDTO);
    }

    @GetMapping
    public ResponseEntity<List<ContactResponseDTO>> getContacts(
            @PathVariable Long applicationId){
        return ResponseEntity.ok(contactService.getContactsByApplicationId(applicationId));
    }

    @DeleteMapping("/{contactId}")
    public ResponseEntity<Void> deleteContact(
            @PathVariable Long applicationId,
            @PathVariable Long contactId){
        contactService.deleteContact(applicationId, contactId);
        return ResponseEntity.noContent().build();
    }
}
