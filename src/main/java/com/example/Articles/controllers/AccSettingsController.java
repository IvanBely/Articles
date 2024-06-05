package com.example.Articles.controllers;

import com.example.Articles.dto.response.UserInfoResponse;
import com.example.Articles.model.User;
import com.example.Articles.model.repository.UserRepository;
import com.example.Articles.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Optional;

@RestController
@RequestMapping("/settings")
public class AccSettingsController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AccountService accountService;
    @GetMapping
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

    @PutMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<String> updateAccountSettings(
            @RequestParam(required = false) String newEmail,
            @RequestParam(required = false) String newUsername,
            @RequestParam(required = false) String newPassword,
            Principal principal) {
        String username = principal.getName();

        Optional<User> userOptional = userRepository.findByUsername(username);

        User user = userOptional.get();
        if (newEmail == null && newUsername == null && newPassword == null) {
            return ResponseEntity.badRequest().body("All parameters are empty");
        }

        // Обновляем данные пользователя, если они были переданы
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
    }

    @DeleteMapping
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
