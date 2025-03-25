package com.example.notes.controller;

import com.example.notes.dto.NoteRequest;
import com.example.notes.model.Note;
import com.example.notes.repository.NoteRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/notes")
public class NoteController {
    private final NoteRepository noteRepository;

    public NoteController(NoteRepository noteRepository) {
        this.noteRepository = noteRepository;
    }

    @GetMapping
    public ResponseEntity<List<Note>> getAllNotes() {
        List<Note> notes = noteRepository.findAll();
        return ResponseEntity.ok(notes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Note> getNoteById(@PathVariable Long id) {
        Optional<Note> note = noteRepository.findById(id);
        return note.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Note> createNote(@Valid @RequestBody NoteRequest noteRequest) {
        Note note = new Note();
        note.setTitle(noteRequest.getTitle());
        note.setContent(noteRequest.getContent());
        Note savedNote = noteRepository.save(note);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedNote);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Note> updateNote(
            @PathVariable Long id,
            @Valid @RequestBody NoteRequest noteRequest) {
        Optional<Note> optionalNote = noteRepository.findById(id);
        if (optionalNote.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Note note = optionalNote.get();
        note.setTitle(noteRequest.getTitle());
        note.setContent(noteRequest.getContent());
        Note updatedNote = noteRepository.save(note);

        return ResponseEntity.ok(updatedNote);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNote(@PathVariable Long id) {
        if (!noteRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        noteRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}