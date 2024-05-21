package com.example.Articles.service;

import com.example.Articles.dto.response.ArticlesResponse;

import java.util.List;
import java.util.Optional;

public interface MainArticleService {
    Optional<ArticlesResponse> getByHash(String hash);
    List<ArticlesResponse> getFirstPublicArticles();
}
