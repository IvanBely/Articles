package com.example.Articles.service;

import com.example.Articles.dto.request.ArticlesRequest;
import com.example.Articles.dto.response.ArticlesResponse;
import com.example.Articles.dto.response.ArticlesUrlResponse;
import com.example.Articles.model.User;

import java.util.List;

public interface AccountArticlesService {
    ArticlesUrlResponse addAndResponseUrl(User user, ArticlesRequest articlesRequest);
    List<ArticlesResponse> getAccountArticles(User user);
    void addUser (User user);
}
