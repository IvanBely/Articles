package com.example.Articles.service;

import com.example.Articles.dto.response.ArticleResponse;
import com.example.Articles.model.Article;

import java.util.List;

public interface MainPageService {
    List<ArticleResponse> getArticleForm(List<Article> articleList);

    List<ArticleResponse> getFirstPublicArticle();

}
