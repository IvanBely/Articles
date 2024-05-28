package com.example.Articles.service;

import com.example.Articles.dto.response.ArticleResponse;
import com.example.Articles.model.Article;

import java.util.List;
import java.util.Optional;

public interface ArticleService {
    Optional<Article> findByHash(String hash);
    Optional<ArticleResponse> getFormByHash(String hash);
    
    List<ArticleResponse> getFirstPublicArticle();
}
