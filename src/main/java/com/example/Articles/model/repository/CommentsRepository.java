package com.example.Articles.model.repository;

import com.example.Articles.model.Comments;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface CommentsRepository extends JpaRepository<Comments, Long> {
    Optional<Comments> findById(Long commentId);
    void deleteById(Long commentId);
}
