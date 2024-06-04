package com.example.Articles.service;

import com.example.Articles.dto.request.ArticleRequest;
import com.example.Articles.dto.response.ArticleResponse;
import com.example.Articles.dto.response.ArticleUrlResponse;
import com.example.Articles.model.Article;
import com.example.Articles.model.User;

import java.util.List;
import java.util.Optional;

public interface ArticleService {
    Optional<Article> findByHash(String hash);
    Optional<ArticleResponse> getArticleResponseByHash(String hash);
    ArticleUrlResponse createArticleAndResponseUrl(User user, ArticleRequest articleRequest);
    void updateArticle(User user, ArticleRequest articleRequest, Article article);
    void deleteArticle(User user, Article article);
}
