package com.example.Articles.service;

import com.example.Articles.dto.response.ArticleResponse;

import java.util.List;

public interface MainPageService {
    List<ArticleResponse> getFirstPublicArticle();
}
