package com.example.Articles.service.impl;

import com.example.Article.service.CreateArticleHash;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CreateArticleHashImpl implements CreateArticleHash {
    @Override
    public String createHashUrl() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString();
    }
}


