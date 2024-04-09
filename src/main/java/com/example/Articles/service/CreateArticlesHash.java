package com.example.Articles.service;

import com.example.Articles.dto.request.ArticlesRequest;

public interface CreateArticlesHash {
    String createHashUrl(ArticlesRequest articlesRequest);
}
