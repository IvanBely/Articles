package com.example.Articles.service;

import com.example.Articles.dto.request.CommentRequest;
import com.example.Articles.model.Article;
import com.example.Articles.model.User;

public interface CommentService {
    void addCommentToArticle(User user, Article article, CommentRequest commentRequest);
    void updateComment(Long articleId, CommentRequest commentRequest);
    void deleteComment(Long articleId);
}
