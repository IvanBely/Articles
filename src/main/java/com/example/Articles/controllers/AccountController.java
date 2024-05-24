package com.example.Articles.controllers;

import com.example.Articles.dto.request.ArticlesRequest;
import com.example.Articles.dto.response.ArticlesResponse;
import com.example.Articles.dto.response.ArticlesUrlResponse;
import com.example.Articles.model.Users;
import com.example.Articles.model.repository.UsersRepository;
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
    private final AccountArticleServiceImpl AccountArticleService;
    @Autowired
    private UsersRepository usersRepository;

    @GetMapping("/{username}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<ArticlesResponse>> getAccountArticles(@PathVariable String username) {
        Optional<Users> userOptional = usersRepository.findByUsername(username);
        if (userOptional.isPresent()) {
            List<ArticlesResponse> articles = AccountArticleService.getAccountArticles(userOptional.get());
            return ResponseEntity.ok(articles);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{username}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ArticlesUrlResponse> add(@PathVariable String username, @RequestBody ArticlesRequest request) {
        Optional<Users> userOptional = usersRepository.findByUsername(username);
        if (userOptional.isPresent()) {
            ArticlesUrlResponse response = AccountArticleService.createArticleAndResponseUrl(userOptional.get(), request);
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
