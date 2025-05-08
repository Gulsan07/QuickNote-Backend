package com.quicknote.controller;

import com.quicknote.model.Note;
import com.quicknote.service.ImageUploadService;
import com.quicknote.service.NoteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/notes")
@Tag(name="Note API", description = "Create, Update, Read, Delete API")
public class NoteController {

    private final NoteService noteService;

    private final ImageUploadService imageUploadService;


    public NoteController(NoteService noteService, ImageUploadService imageUploadService) {
        this.noteService = noteService;
        this.imageUploadService = imageUploadService;
    }

    @PostMapping("/create-note")
    @Operation(summary = "Create a note")
    public ResponseEntity<Note> createNote(
            @RequestParam("title") String title,
            @RequestParam("content") String content,
            @RequestParam("category") String category,
            @RequestParam(value = "image", required = false) MultipartFile image,
            @AuthenticationPrincipal UserDetails userDetails) {

        String imageUrl = null;

        // upload image to Cloudinary (if provided)
        if (image != null && !image.isEmpty()) {
            imageUrl = imageUploadService.uploadImage(image);  // Ensure this service is implemented
        }

        System.out.println(image);

        Note newNote = new Note();
        newNote.setTitle(title);
        newNote.setContent(content);
        newNote.setCategory(category);
        newNote.setImageUrl(imageUrl);

        return ResponseEntity.ok(noteService.createNote(newNote, userDetails.getUsername()));
    }


    @GetMapping("/get-user-note")
    @Operation(summary = "Get all note of a User ")
    public ResponseEntity<List<Note>> getUserNote(@AuthenticationPrincipal UserDetails userDetails){
        System.out.println("Calling get note ");
        return ResponseEntity.ok(noteService.getUserNote(userDetails.getUsername()));
    }



    @PutMapping("/update-note/{id}")
    @Operation(summary = "Update note of a User ")
    public ResponseEntity<Note> updateNote(@RequestBody Note note, @PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails){
        System.out.println("###########################");
        return ResponseEntity.ok(noteService.updateNote(id, note, userDetails.getUsername()));
    }

    @DeleteMapping("/delete-note/{id}")
    @Operation(summary = "Delete note of a User ")
    public ResponseEntity<String> deleteNote(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails){
        return ResponseEntity.ok(noteService.deleteNote(id, userDetails.getUsername()));
    }


    @GetMapping("/search")
    @Operation(summary = "Search note of a User by Title ")
    public ResponseEntity<List<Note>> searchNotes(
            @RequestParam String keyword){
        return ResponseEntity.ok(noteService.searchNote(keyword));
    }

    @GetMapping("/filter")
    @Operation(summary = "Search note of a User by Category")
    public ResponseEntity<List<Note>> filterNoteByCategory(
            @RequestParam String caregory,
            @RequestParam Long userId
    ){
        return ResponseEntity.ok(noteService.filterNoteByCategory(userId, caregory));
    }

    @GetMapping("/sort")
    @Operation(summary = "Filter note of a User in Acending and Decending ")
    public ResponseEntity<List<Note>> sortNotes(
            @RequestParam String sortBy,
            @RequestParam Long userId
    ){
        return ResponseEntity.ok(noteService.getSortedNote(userId, sortBy));
    }


    @GetMapping("/debug/auth")
    public ResponseEntity<String> debugAuth() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("No authentication found.");
        }
        return ResponseEntity.ok("Authenticated user: " + authentication.getName());
    }
}
