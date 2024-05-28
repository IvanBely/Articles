package com.example.Articles.controllers;

import com.example.Articles.dto.request.CommentRequest;
import com.example.Articles.dto.response.ArticleResponse;
import com.example.Articles.model.Article;
import com.example.Articles.model.User;
import com.example.Articles.model.repository.CommentRepository;
import com.example.Articles.service.AccountArticleService;
import com.example.Articles.service.ArticleService;
import com.example.Articles.service.CommentArticleService;
import com.example.Articles.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/Article")
@AllArgsConstructor
public class ArticleController {
    private final ArticleService articleservice;
    private final AccountArticleService accountArticleService;
    private final UserService userService;
    private final CommentRepository CommentRepository;
    private final CommentArticleService commentArticleService;

    @GetMapping("/")
    public ResponseEntity<List<ArticleResponse>> getPublicList() {
        List<ArticleResponse> ArticleResponseList = articleservice.getFirstPublicArticle();
        return ResponseEntity.ok(ArticleResponseList);
    }
    @GetMapping("/{hash}")
    public ResponseEntity<ArticleResponse> getByHash(@PathVariable String hash) {
        Optional<ArticleResponse> optionalArticleResponse = articleservice.getFormByHash(hash);
        if (optionalArticleResponse.isPresent()) {
            return ResponseEntity.ok(optionalArticleResponse.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }
//    @PostMapping("/{hash}") // лайк
//    @PreAuthorize("isAuthenticated()")
//    public ResponseEntity<ArticleResponse> getByHash(@PathVariable String hash) {
//        Optional<ArticleResponse> optionalArticleResponse = Articleervice.getByHash(hash);
//        if (optionalArticleResponse.isPresent()) {
//            return ResponseEntity.ok(optionalArticleResponse.get());
//        } else {
//            return ResponseEntity.notFound().build();
//        }
//    }
    @PostMapping("/{hash}")
    public ResponseEntity<String> addCommentToArticle(@PathVariable String hash, @RequestBody CommentRequest commentRequest, Principal principal) {
        String username = principal.getName();
        String commentText = commentRequest.getText();
        // Ищем статью по ее хешу
        Optional<Article> optionalArticle = articleservice.findByHash(hash);
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
    public ResponseEntity<String> updateCommentInArticle(@PathVariable String hash, @PathVariable Long commentId, @RequestBody CommentRequest commentRequest, Principal principal) {
        // Получаем имя пользователя из Principal
        String username = principal.getName();
        String commentText = commentRequest.getText();
        // Ищем статью по ее хешу
        Optional<Article> optionalArticle = articleservice.findByHash(hash);
        Optional<User> optionalUser = userService.findByUsername(username);
        if (optionalArticle.isPresent() && optionalUser.isPresent()) {
            // Получаем статью
            Article article = optionalArticle.get();
            // Получаем пользователя
            User user = optionalUser.get();
            // Обновляем комментарий к статье
            commentArticleService.updateComment(article.getId(), );
            return ResponseEntity.ok("Comment updated successfully.");
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{hash}")
    public ResponseEntity<String> deleteCommentFromArticle(@PathVariable String hash, @PathVariable Long commentId, Principal principal) {
        // Получаем имя пользователя из Principal
        String username = principal.getName();
        // Ищем статью по ее хешу и находим юзера по имени
        Optional<Article> optionalArticle = articleservice.findByHash(hash);
        Optional<User> optionalUser = userService.findByUsername(username);
        if (optionalArticle.isPresent() && optionalUser.isPresent()) {
            // Получаем статью
            Article article = optionalArticle.get();
            // Получаем пользователя
            User user = optionalUser.get();
            // Удаляем комментарий из статьи
            commentArticleService.deleteComment(user, article, commentId);
            return ResponseEntity.ok("Comment deleted successfully.");
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
