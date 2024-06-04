package com.example.Articles.service;

import com.example.Articles.dto.request.ArticleRequest;
import com.example.Articles.dto.response.ArticleResponse;
import com.example.Articles.dto.response.ArticleUrlResponse;
import com.example.Articles.dto.response.UserInfoResponse;
import com.example.Articles.model.Article;
import com.example.Articles.model.User;

import java.util.List;

public interface AccountService {
    List<ArticleResponse> getAccountArticle(User user);
    UserInfoResponse getUserInfo(User user);
    void setEmail(User user, String email);
    void setUsername(User user, String username);
    void setPassword(User user, String password);
    void deleteAccount(User user);

}
