package com.example.Articles.controllers;

import com.example.Articles.dto.request.ArticleRequest;
import com.example.Articles.dto.response.ArticleResponse;
import com.example.Articles.dto.response.ArticleUrlResponse;
import com.example.Articles.model.Article;
import com.example.Articles.model.User;
import com.example.Articles.model.repository.ArticleRepository;
import com.example.Articles.model.repository.UserRepository;
import com.example.Articles.service.AccountService;
import com.example.Articles.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Optional;
@RestController
@RequestMapping("/u/{username}")
public class AccountController {
    @Autowired
    private AccountService accountService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ArticleRepository articleRepository;
    @Autowired
    private ArticleService articleService;

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<ArticleResponse>> getAccountArticle(@PathVariable String username) {
        Optional<User> userOptional = userRepository.findByUsername(username);
        if (userOptional.isPresent()) {
            List<ArticleResponse> Article = accountService.getAccountArticle(userOptional.get());
            return ResponseEntity.ok(Article);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @PostMapping("/editor")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ArticleUrlResponse> createArticle(
            @RequestBody ArticleRequest articleRequest,
            Principal principal) {
        String username = principal.getName();
        Optional<User> userOptional = userRepository.findByUsername(username);
        ArticleUrlResponse response = articleService.createArticleAndResponseUrl(userOptional.get(), articleRequest);

        if (response != null) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    @PutMapping("/editor")
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
    @DeleteMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<String> deleteArticle(
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
