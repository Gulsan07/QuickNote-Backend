package com.quicknote.service;

import com.quicknote.model.Note;
import com.quicknote.model.User;
import com.quicknote.repository.NoteRepository;
import com.quicknote.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoteService {

    private final NoteRepository noteRepository;
    private final UserRepository userRepository;

    public NoteService(NoteRepository noteRepository, UserRepository userRepository) {
        this.noteRepository = noteRepository;
        this.userRepository = userRepository;
    }

    public Note createNote(Note note, String username){

        if (noteRepository.existsByContent(note.getContent())){
            return new Note();
        }

        System.out.println(username+"In Servece");
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new RuntimeException("user not found ...."));

        note.setUser(user);
        return noteRepository.save(note);

    }

    public List<Note> getUserNote(String username){
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("user not found ....."));
        return noteRepository.findByUserId(user.getId());
    }

    public Note updateNote(Long noteId, Note updatedNote, String username){
        System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$");

        Note note = noteRepository.findById(noteId)
                .orElseThrow(() -> new RuntimeException("note not found ...."));
        System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
        if(!note.getUser().getEmail().equals(username)){
            throw new RuntimeException("Unauthorized to update this note ...");
        }

        note.setTitle(updatedNote.getTitle());
        note.setContent(updatedNote.getContent());
        note.setCategory(updatedNote.getCategory());
        note.setImageUrl(updatedNote.getImageUrl());

        return noteRepository.save(note);

    }

    public String  deleteNote(Long noteId, String username){
        Note note = noteRepository.findById(noteId)
                .orElseThrow(() -> new RuntimeException("note not found"));
        System.out.println(note.getUser().getEmail()+" >>>>>>>>>> "+ username);
        if (!note.getUser().getEmail().equals(username)){
//            throw new RuntimeException("Unauthorized to delete this note");
            System.out.println("Unauthorized to delete this note");
        }

        noteRepository.delete(note);
        return "Note deleted successfully ...";
    }




   public List<Note> searchNote(String keyword){
        return noteRepository.searchNotes(keyword);
    }

   public List<Note> filterNoteByCategory(Long userId, String category){
        return noteRepository.findByUserIdAndCategoryIgnoreCase(userId, category);
    }

    public List<Note> getSortedNote(Long userId, String sortBy){
        if ("updated".equalsIgnoreCase(sortBy)){
            return noteRepository.findByUserIdOrderByUpdatedAtDesc(userId);
        } else if ("created".equalsIgnoreCase(sortBy)) {
            return noteRepository.findByUserIdOrderByCreatedAtDesc(userId);

        }
        return noteRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }

}
