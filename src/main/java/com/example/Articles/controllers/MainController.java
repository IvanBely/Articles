package com.example.Articles.controllers;

import com.example.Articles.dto.response.ArticlesResponse;
import com.example.Articles.model.User;
import com.example.Articles.service.impl.AccountArticlesServiceImpl;
import com.example.Articles.service.impl.MainArticleServiceImpl;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@AllArgsConstructor
public class MainController {
    private final MainArticleServiceImpl mainArticleService;
    private final AccountArticlesServiceImpl accountArticlesService;

    @GetMapping("/")
    public ResponseEntity<List<ArticlesResponse>> getPublicList() {
        List<ArticlesResponse> articlesResponseList = mainArticleService.getFirstPublicArticles();
        return ResponseEntity.ok(articlesResponseList);
    }
    @GetMapping("/{hash}")
    public ResponseEntity<ArticlesResponse> getByHash(@PathVariable String hash) {
        Optional<ArticlesResponse> optionalArticlesResponse = mainArticleService.getByHash(hash);
        if (optionalArticlesResponse.isPresent()) {
            return ResponseEntity.ok(optionalArticlesResponse.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @PostMapping("/new-user")
    public ResponseEntity<String> addUser(@RequestBody User user) {
        accountArticlesService.addUser(user);
        return new ResponseEntity<>("User is saved", HttpStatus.CREATED);
    }
}
