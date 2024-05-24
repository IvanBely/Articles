package com.example.Articles.service;

import com.example.Articles.dto.response.ArticlesResponse;
import com.example.Articles.model.Articles;

import java.util.List;
import java.util.Optional;

public interface MainArticleService {
    Optional<Articles> findByHash(String hash);
    Optional<ArticlesResponse> getFormByHash(String hash);
    
    List<ArticlesResponse> getFirstPublicArticles();
}
