package com.example.Articles.controllers;

import com.example.Articles.dto.request.CommentRequest;
import com.example.Articles.dto.response.ArticleResponse;
import com.example.Articles.model.Article;
import com.example.Articles.model.Comment;
import com.example.Articles.model.User;
import com.example.Articles.model.repository.CommentRepository;
import com.example.Articles.service.ArticleService;
import com.example.Articles.service.CommentArticleService;
import com.example.Articles.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Optional;
@RestController
public class ArticleController {
    @Autowired
    private ArticleService articleService;
    @Autowired
    private UserService userService;
    @Autowired
    private CommentArticleService commentArticleService;
    @Autowired
    private CommentRepository commentRepository;

    @GetMapping("/")
    public ResponseEntity<List<ArticleResponse>> getPublicList() {
        List<ArticleResponse> ArticleResponseList = articleService.getFirstPublicArticle();
        return ResponseEntity.ok(ArticleResponseList);
    }
    @GetMapping("/{hash}")
    public ResponseEntity<ArticleResponse> getByHash(
            @PathVariable String hash) {
        Optional<ArticleResponse> optionalArticleResponse = articleService.getArticleResponseByHash(hash);
        if (optionalArticleResponse.isPresent()) {
            return ResponseEntity.ok(optionalArticleResponse.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @PostMapping("/{hash}")
    public ResponseEntity<String> addCommentToArticle(
            @PathVariable String hash,
            @RequestBody CommentRequest commentRequest,
            Principal principal) {
        String username = principal.getName();
        String commentText = commentRequest.getText();
        // Ищем статью по ее хешу
        Optional<Article> optionalArticle = articleService.findByHash(hash);
        Optional<User> optionalUser = userService.findByUsername(username);
        if (optionalArticle.isPresent() && optionalUser.isPresent()) {
            // Получаем статью
            Article article = optionalArticle.get();
            // Получаем пользователя
            User user = optionalUser.get();
            // Добавляем комментарий к статье
            commentArticleService.addCommentToArticle(user, article, commentText);
            return ResponseEntity.ok("Comment added successfully.");
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @PutMapping("/{hash}")
    public ResponseEntity<String> updateCommentInArticle(
            @PathVariable String hash,
            @PathVariable Long commentId,
            @RequestBody CommentRequest commentRequest,
            @AuthenticationPrincipal User user) {

        String commentText = commentRequest.getText();
        Optional<Comment> optionalComment = commentRepository.findById(commentId);

        if (optionalComment.isPresent() && optionalComment.get().getUser().equals(user)) {
            commentArticleService.updateComment(commentId, commentText);
            return ResponseEntity.ok("Comment updated successfully.");
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{hash}")
    public ResponseEntity<String> deleteCommentFromArticle(
            @PathVariable Long commentId,
            @AuthenticationPrincipal User user) {
        Optional<Comment> optionalComment = commentRepository.findById(commentId);
        if (optionalComment.isPresent() && optionalComment.get().getUser().equals(user)) {

            commentArticleService.deleteComment(commentId);
            return ResponseEntity.ok("Comment deleted successfully.");
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
