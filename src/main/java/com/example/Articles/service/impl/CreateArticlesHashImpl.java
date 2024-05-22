package com.example.Articles.service.impl;

import com.example.Articles.service.CreateArticlesHash;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CreateArticlesHashImpl implements CreateArticlesHash {
    @Override
    public String createHashUrl() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString();
    }
}


