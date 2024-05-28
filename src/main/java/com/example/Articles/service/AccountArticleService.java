package com.example.Articles.service;

import com.example.Articles.dto.request.ArticleRequest;
import com.example.Articles.dto.response.ArticleResponse;
import com.example.Articles.dto.response.ArticleUrlResponse;
import com.example.Articles.model.User;

import java.util.List;

public interface AccountArticleService {
    ArticleUrlResponse createArticleAndResponseUrl(User user, ArticleRequest articleRequest);
    List<ArticleResponse> getAccountArticle(User user);
}
