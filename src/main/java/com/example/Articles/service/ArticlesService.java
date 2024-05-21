package com.example.Articles.service;

import com.example.Articles.dto.request.ArticlesRequest;
import com.example.Articles.dto.response.ArticlesResponse;
import com.example.Articles.dto.response.ArticlesUrlResponse;
import com.example.Articles.model.Users;

import java.util.List;
import java.util.Optional;

public interface ArticlesService {
    Optional<ArticlesResponse> getByHash(String hash);
    List<ArticlesResponse> getFirstPublicArticles();
    ArticlesUrlResponse addAndResponseUrl(Users user, ArticlesRequest articlesRequest);
    List<ArticlesResponse> getAccountArticles(Users user);
}
