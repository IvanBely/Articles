package com.example.Articles.service;

import com.example.Articles.dto.request.ArticlesRequest;
import com.example.Articles.dto.response.ArticlesResponse;
import com.example.Articles.dto.response.ArticlesUrlResponse;
import com.example.Articles.model.Users;

import java.util.List;

public interface AccountArticleService {
    ArticlesUrlResponse createArticleAndResponseUrl(Users user, ArticlesRequest articlesRequest);
    List<ArticlesResponse> getAccountArticles(Users user);
    void addUser (Users user);

}
