package com.example.Articles.controllers;

import com.example.Articles.config.security.UserDetailsImpl;
import com.example.Articles.dto.request.ArticleRequest;
import com.example.Articles.dto.response.ArticleResponse;
import com.example.Articles.dto.response.ArticleUrlResponse;
import com.example.Articles.dto.response.UserInfoResponse;
import com.example.Articles.model.Article;
import com.example.Articles.model.Comment;
import com.example.Articles.model.User;
import com.example.Articles.model.repository.ArticleRepository;
import com.example.Articles.model.repository.UserRepository;

import com.example.Articles.service.AccountService;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Optional;
@RestController
public class AccountController {
    @Autowired
    private AccountService accountService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ArticleRepository articleRepository;

    @GetMapping("/{username}")
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

    @GetMapping("/{username}/settings")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<UserInfoResponse> getUserInfo(
            Principal principal) {
        String username = principal.getName();
        Optional<User> userOptional = userRepository.findByUsername(username);
        if (userOptional.isPresent()) {
            UserInfoResponse userInfoResponse = accountService.getUserInfo(userOptional.get());
            return ResponseEntity.ok(userInfoResponse);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{username}/settings")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<String> updateAccountSettings(
            @PathVariable String username,
            @RequestParam(required = false) String newEmail,
            @RequestParam(required = false) String newUsername,
            @RequestParam(required = false) String newPassword,
            Principal principal) {
        String currentUsername = principal.getName();

        Optional<User> userOptional = userRepository.findByUsername(username);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if (newEmail != null) {
                accountService.setEmail(user, newEmail);
            }
            if (newUsername != null) {
                accountService.setUsername(user, newUsername);
            }
            if (newPassword != null) {
                accountService.setPassword(user, newPassword);
            }
            return ResponseEntity.ok("Account settings updated");
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{username}/settings")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<String> deleteAccount(
            Principal principal) {
        String username = principal.getName();
        Optional<User> userOptional = userRepository.findByUsername(username);
        if (userOptional.isPresent()) {
            accountService.deleteAccount(userOptional.get());
            return ResponseEntity.ok("Account deleted");
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
