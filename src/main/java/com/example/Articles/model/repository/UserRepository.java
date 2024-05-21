package com.example.Articles.model.repository;

import com.example.Articles.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByAccountName(String accountName);
}
