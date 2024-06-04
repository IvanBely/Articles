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
}
