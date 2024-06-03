package com.example.Articles.controllers;

import com.example.Articles.dto.request.ArticleRequest;
import com.example.Articles.dto.response.ArticleResponse;
import com.example.Articles.dto.response.ArticleUrlResponse;
import com.example.Articles.model.User;
import com.example.Articles.model.repository.UserRepository;
import com.example.Articles.service.AccountArticleService;
import com.example.Articles.service.impl.AccountArticleServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
@RestController
@RequestMapping("/user")
public class AccountController {
    @Autowired
    private AccountArticleService accountArticleservice;
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/{username}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<ArticleResponse>> getAccountArticle(@PathVariable String username) {
        Optional<User> userOptional = userRepository.findByUsername(username);
        if (userOptional.isPresent()) {
            List<ArticleResponse> Article = accountArticleservice.getAccountArticle(userOptional.get());
            return ResponseEntity.ok(Article);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{username}")
    public ResponseEntity<ArticleUrlResponse> add(
            @RequestBody ArticleRequest articleRequest,
            @AuthenticationPrincipal User user) {
        ArticleUrlResponse response = accountArticleservice.createArticleAndResponseUrl(user, articleRequest);

        if (response != null) {
            return ResponseEntity.ok(response); // Возвращает ответ с URL
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); // Возвращает ошибку 500
        }
    }
}
