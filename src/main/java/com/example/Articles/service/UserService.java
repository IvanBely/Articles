package com.example.Articles.service;

import com.example.Articles.model.User;

import java.util.Optional;

public interface UserService {
    void addUser(User user);
    Optional<User> findByUsername (String username);
}
