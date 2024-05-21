package com.example.Articles.controllers;

import com.example.Articles.dto.request.ArticlesRequest;
import com.example.Articles.dto.response.ArticlesResponse;
import com.example.Articles.dto.response.ArticlesUrlResponse;
import com.example.Articles.model.User;
import com.example.Articles.model.repository.UserRepository;
import com.example.Articles.service.impl.AccountArticlesServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api")
@AllArgsConstructor
public class AccountController {
    private final AccountArticlesServiceImpl accountArticlesService;
    private final UserRepository userRepository;

    @GetMapping("/{accountName}")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public ResponseEntity<List<ArticlesResponse>> getAccountArticles(@PathVariable String accountName) {
        Optional<User> userOptional = userRepository.findByAccountName(accountName);
        if (userOptional.isPresent()) {
            List<ArticlesResponse> articles = accountArticlesService.getAccountArticles(userOptional.get());
            if (!articles.isEmpty()) {
                return ResponseEntity.ok(articles);
            } else {
                return ResponseEntity.notFound().build();
            }
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{accountName}")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public ResponseEntity<ArticlesUrlResponse> add(@PathVariable String accountName, @RequestBody ArticlesRequest request) {
        Optional<User> userOptional = userRepository.findByAccountName(accountName);
        if (userOptional.isPresent()) {
            ArticlesUrlResponse response = accountArticlesService.addAndResponseUrl(userOptional.get(), request);
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
