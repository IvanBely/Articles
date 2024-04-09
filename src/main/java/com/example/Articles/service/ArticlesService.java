package com.example.Articles.service;

import com.example.Articles.dto.request.ArticlesRequest;
import com.example.Articles.dto.response.ArticlesResponse;
import com.example.Articles.dto.response.ArticlesUrlResponse;

import java.util.List;

public interface ArticlesService {
    ArticlesResponse getByHash(String hash);
    List<ArticlesResponse> getFirstPublicArticles();
    ArticlesUrlResponse addAndResponseUrl(ArticlesRequest articlesRequest);
}
