package com.quicknote.repository;

import com.quicknote.model.Note;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NoteRepository extends JpaRepository<Note, Long> {
    List<Note> findByUserId(Long userId);
    List<Note> findByTitleContainingIgnoreCaseOrContentContainingIgnoreCase(String title, String content);
    Boolean existsByContent(String content);

    @Query("SELECT n FROM Note n WHERE " +
            "(LOWER(n.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(n.content) LIKE LOWER(CONCAT('%', :keyword, '%'))) ")
    List<Note> searchNotes(@Param("keyword") String keyword);

    List<Note> findByUserIdAndCategoryIgnoreCase(Long userId, String category);

    List<Note> findByUserIdOrderByCreatedAtDesc(Long userId);

    List<Note> findByUserIdOrderByUpdatedAtDesc(Long userId);

}
