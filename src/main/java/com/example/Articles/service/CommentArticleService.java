package com.example.Articles.service;

import com.example.Articles.dto.request.ArticleRequest;
import com.example.Articles.model.Article;
import com.example.Articles.model.User;

public interface CommentArticleService {
    void addCommentToArticle(User user, Article article, String commentText);
    void updateComment(Long articleId, String newCommentText);
    void deleteComment(Long articleId);
}
