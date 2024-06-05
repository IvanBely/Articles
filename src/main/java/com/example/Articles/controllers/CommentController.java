package com.example.Articles.controllers;

import com.example.Articles.dto.request.CommentRequest;
import com.example.Articles.model.Article;
import com.example.Articles.model.Comment;
import com.example.Articles.model.User;
import com.example.Articles.model.repository.CommentRepository;
import com.example.Articles.model.repository.UserRepository;
import com.example.Articles.service.ArticleService;
import com.example.Articles.service.CommentService;
import com.example.Articles.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Optional;

@RestController
public class CommentController {
    @Autowired
    private ArticleService articleService;
    @Autowired
    private UserService userService;
    @Autowired
    private CommentService commentService;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private UserRepository userRepository;
    @PostMapping("/{hash}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<String> addCommentToArticle(
            @PathVariable String hash,
            @RequestBody CommentRequest commentRequest,
            Principal principal) {

        Optional<Article> optionalArticle = articleService.findByHash(hash);

        String username = principal.getName();
        Optional<User> userOptional = userRepository.findByUsername(username);

        if (optionalArticle.isPresent() && userOptional.isPresent()) {
            // Получаем статью
            Article article = optionalArticle.get();
            User user = userOptional.get();
            // Добавляем комментарий к статье
            commentService.addCommentToArticle(user, article, commentRequest);
            return ResponseEntity.ok("Comment added successfully.");
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @PutMapping("/{hash}")
    public ResponseEntity<String> updateCommentInArticle(
            @PathVariable String hash,
            @RequestParam Long commentId,
            @RequestBody CommentRequest commentRequest,
            Principal principal) {

        Optional<Comment> optionalComment = commentRepository.findById(commentId);

        String username = principal.getName();
        Optional<User> userOptional = userRepository.findByUsername(username);

        if (optionalComment.isPresent() && optionalComment.get().getUser().equals(userOptional.get())) {
            commentService.updateComment(commentId, commentRequest);
            return ResponseEntity.ok("Comment updated successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You are not authorized to update this comment.");
        }
    }

    @DeleteMapping("/{hash}")
    public ResponseEntity<String> deleteCommentFromArticle(
            @RequestParam Long commentId,
            Principal principal) {

        Optional<Comment> optionalComment = commentRepository.findById(commentId);

        String username = principal.getName();
        Optional<User> userOptional = userRepository.findByUsername(username);

        if (optionalComment.isPresent() && optionalComment.get().getUser().equals(userOptional.get())) {
            commentService.deleteComment(commentId);
            return ResponseEntity.ok("Comment deleted successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You are not authorized to delete this comment.");
        }
    }
}
