package com.example.Articles.model.repository;

import com.example.Articles.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsersRepository extends JpaRepository<Users, Long> {
    Optional<Users> findByAccountName(String accountName);
}
