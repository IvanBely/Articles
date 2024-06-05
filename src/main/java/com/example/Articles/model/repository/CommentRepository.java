package com.example.Articles.model.repository;

import com.example.Articles.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    Optional<Comment> findById(Long commentId);
    List<Comment> findAllByUserId(Long userId);
    List<Comment> findAllByArticleId(Long articleId);
    void deleteById(Long commentId);

}
