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
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class AccountController {
    private final AccountArticleService accountArticleservice;
    @Autowired
    private UserRepository UserRepository;

    @GetMapping("/{username}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<ArticleResponse>> getAccountArticle(@PathVariable String username) {
        Optional<User> userOptional = UserRepository.findByUsername(username);
        if (userOptional.isPresent()) {
            List<ArticleResponse> Article = accountArticleservice.getAccountArticle(userOptional.get());
            return ResponseEntity.ok(Article);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{username}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ArticleUrlResponse> add(@PathVariable String username, @RequestBody ArticleRequest articleRequest) {
        Optional<User> userOptional = UserRepository.findByUsername(username);
        if (userOptional.isPresent()) {
            ArticleUrlResponse response = accountArticleservice.createArticleAndResponseUrl(userOptional.get(), articleRequest);
            if (response != null) {
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.badRequest().build();
            }
        } else {
            // Если пользователь не найден, возвращаем 404 Not Found
            return ResponseEntity.notFound().build();
        }
    }
}
