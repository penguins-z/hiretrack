package com.ferdin.hiretrack.service;

import com.ferdin.hiretrack.dto.NoteRequestDTO;
import com.ferdin.hiretrack.dto.NoteResponseDTO;
import com.ferdin.hiretrack.entity.Application;
import com.ferdin.hiretrack.entity.Note;
import com.ferdin.hiretrack.exception.ResourceNotFoundException;
import com.ferdin.hiretrack.repository.ApplicationRepository;
import com.ferdin.hiretrack.repository.NoteRepository;
import com.ferdin.hiretrack.security.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NoteService {

    private final NoteRepository noteRepository;
    private final ApplicationRepository applicationRepository;
    private final SecurityUtils securityUtils;

    @Transactional
    public NoteResponseDTO addNote(Long applicationId, NoteRequestDTO requestDTO) {

        Long userId = securityUtils.getCurrentUserId();

        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Application not found with id: " + applicationId));

        if (!application.getUser().getId().equals(userId)) {
            throw new ResourceNotFoundException(
                    "Application not found with id: " + applicationId);
        }

        Note note = new Note();
        note.setContent(requestDTO.getContent());
        note.setCreatedAt(LocalDateTime.now());
        note.setApplication(application);

        Note saved = noteRepository.save(note);
        return toResponseDTO(saved);
    }

    public List<NoteResponseDTO> getNotesByApplication(Long applicationId) {

        Long userId = securityUtils.getCurrentUserId();

        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Application not found with id: " + applicationId));

        if (!application.getUser().getId().equals(userId)) {
            throw new ResourceNotFoundException(
                    "Application not found with id: " + applicationId);
        }

        return noteRepository.findByApplicationId(applicationId)
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteNote(Long applicationId, Long noteId) {

        Long userId = securityUtils.getCurrentUserId();

        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Application not found with id: " + applicationId));

        if (!application.getUser().getId().equals(userId)) {
            throw new ResourceNotFoundException(
                    "Application not found with id: " + applicationId);
        }

        Note note = noteRepository.findById(noteId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Note not found with id: " + noteId));

        if (!note.getApplication().getId().equals(applicationId)) {
            throw new ResourceNotFoundException(
                    "Note not found with id: " + noteId);
        }

        noteRepository.delete(note);
    }

    private NoteResponseDTO toResponseDTO(Note note) {
        NoteResponseDTO dto = new NoteResponseDTO();
        dto.setId(note.getId());
        dto.setContent(note.getContent());
        dto.setCreatedAt(note.getCreatedAt());
        return dto;
    }
}