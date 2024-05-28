package com.example.Articles.service.impl;

import com.example.Articles.model.Article;
import com.example.Articles.model.User;
import com.example.Articles.model.repository.UserRepository;
import com.example.Articles.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    @Override
    public void addUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }
    public Optional<User> findByUsername (String username) {
        return userRepository.findByUsername(username);
    }
}
