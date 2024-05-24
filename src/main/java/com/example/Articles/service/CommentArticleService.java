package com.example.Articles.service;

import com.example.Articles.dto.request.ArticlesRequest;
import com.example.Articles.model.Articles;
import com.example.Articles.model.Users;

public interface CommentArticleService {
    void addCommentToArticle(Users user, Articles article, String commentText);
    void createComment(Users user, ArticlesRequest articlesRequest);
    void updateComment(Long articleId, ArticlesRequest updatedArticle);
    void deleteComment(Long articleId);
}
