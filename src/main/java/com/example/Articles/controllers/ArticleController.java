package com.example.Articles.controllers;

import com.example.Articles.dto.request.ArticleRequest;
import com.example.Articles.dto.request.CommentRequest;
import com.example.Articles.dto.response.ArticleResponse;
import com.example.Articles.dto.response.ArticleUrlResponse;
import com.example.Articles.model.Article;
import com.example.Articles.model.Comment;
import com.example.Articles.model.User;
import com.example.Articles.model.repository.ArticleRepository;
import com.example.Articles.model.repository.CommentRepository;
import com.example.Articles.model.repository.UserRepository;

import com.example.Articles.service.AccountService;
import com.example.Articles.service.ArticleService;

import com.example.Articles.service.CommentService;
import com.example.Articles.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
    private UserRepository userRepository;
    @Autowired
    private AccountService accountService;
    @Autowired
    private ArticleRepository articleRepository;

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
    @PostMapping("/{username}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ArticleUrlResponse> createArticle(
            @RequestBody ArticleRequest articleRequest,
            Principal principal) {
        String username = principal.getName();
        Optional<User> userOptional = userRepository.findByUsername(username);
        ArticleUrlResponse response = articleService.createArticleAndResponseUrl(userOptional.get(), articleRequest);

        if (response != null) {
            return ResponseEntity.ok(response); // Возвращает ответ с URL
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); // Возвращает ошибку 500
        }
    }
    @PutMapping("/{username}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<String> updateArticle(
            @RequestBody ArticleRequest articleRequest,
            @RequestParam Long articleId,
            Principal principal) {

        String username = principal.getName();
        Optional<User> userOptional = userRepository.findByUsername(username);

        Optional<Article> optionalArticle = articleRepository.findById(articleId);

        if (userOptional.isPresent() && optionalArticle.isPresent()) {

            articleService.updateArticle(userOptional.get(), articleRequest, optionalArticle.get());
            return ResponseEntity.ok("Article update successfully.");
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @DeleteMapping("/{username}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<String> deleteArticle(
            @RequestBody ArticleRequest articleRequest,
            @RequestParam Long articleId,
            Principal principal) {

        String username = principal.getName();
        Optional<User> userOptional = userRepository.findByUsername(username);

        Optional<Article> optionalArticle = articleRepository.findById(articleId);

        if (userOptional.isPresent() && optionalArticle.isPresent()) {

            articleService.deleteArticle(userOptional.get(), optionalArticle.get());
            return ResponseEntity.ok("Article deleted successfully.");
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
