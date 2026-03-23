package com.ferdin.hiretrack.controller;

import com.ferdin.hiretrack.dto.NoteRequestDTO;
import com.ferdin.hiretrack.dto.NoteResponseDTO;
import com.ferdin.hiretrack.service.NoteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/applications/{applicationId}/notes")
@RequiredArgsConstructor
public class NoteController {

    private final NoteService noteService;

    @PostMapping
    public ResponseEntity<NoteResponseDTO> addNote(
            @PathVariable Long applicationId,
            @Valid @RequestBody NoteRequestDTO requestDTO) {
        NoteResponseDTO created = noteService.addNote(applicationId, requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping
    public ResponseEntity<List<NoteResponseDTO>> getNotes(
            @PathVariable Long applicationId) {
        return ResponseEntity.ok(noteService.getNotesByApplication(applicationId));
    }

    @DeleteMapping("/{noteId}")
    public ResponseEntity<Void> deleteNote(
            @PathVariable Long applicationId,
            @PathVariable Long noteId) {
        noteService.deleteNote(applicationId, noteId);
        return ResponseEntity.noContent().build();
    }
}
