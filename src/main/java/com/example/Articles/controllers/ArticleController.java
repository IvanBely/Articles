package com.example.Articles.controllers;

import com.example.Articles.dto.response.ArticleResponse;
import com.example.Articles.model.repository.ArticleRepository;
import com.example.Articles.model.repository.UserRepository;
import com.example.Articles.service.AccountService;
import com.example.Articles.service.ArticleService;
import com.example.Articles.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

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
}
