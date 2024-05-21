package com.example.Articles.controllers;

import com.example.Articles.dto.request.ArticlesRequest;
import com.example.Articles.dto.response.ArticlesResponse;
import com.example.Articles.dto.response.ArticlesUrlResponse;
import com.example.Articles.model.Users;
import com.example.Articles.model.repository.UsersRepository;
import com.example.Articles.service.impl.ArticlesServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class AccountController {
    private final ArticlesServiceImpl articlesService;
    private final UsersRepository usersRepository;

    @GetMapping("/{accountName}") // ToDo // только зарегестрированному
    public ResponseEntity<List<ArticlesResponse>> getAccountArticles(@PathVariable String accountName) {
        Optional<Users> userOptional = usersRepository.findByAccountName(accountName);
        if (userOptional.isPresent()) {
            List<ArticlesResponse> articles = articlesService.getAccountArticles(userOptional.get());
            if (!articles.isEmpty()) {
                return ResponseEntity.ok(articles);
            } else {
                return ResponseEntity.notFound().build();
            }
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{accountName}") // ToDo // только зарегестрированному
    public ResponseEntity<ArticlesUrlResponse> add(@PathVariable String accountName, @RequestBody ArticlesRequest request) {
        Optional<Users> userOptional = usersRepository.findByAccountName(accountName);
        if (userOptional.isPresent()) {
            ArticlesUrlResponse response = articlesService.addAndResponseUrl(userOptional.get(), request);
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
