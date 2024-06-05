package com.example.Articles.service.impl;

import com.example.Articles.dto.request.CommentRequest;
import com.example.Articles.model.Article;
import com.example.Articles.model.Comment;
import com.example.Articles.model.User;
import com.example.Articles.model.repository.ArticleRepository;
import com.example.Articles.model.repository.CommentRepository;
import com.example.Articles.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final ArticleRepository articleRepository;
    @Override
    public void addCommentToArticle(User user, Article article, CommentRequest commentRequest) {
        String commentText = commentRequest.getText();
        Comment comment = new Comment();
        comment.setArticle(article);
        comment.setCommentText(commentText);
        comment.setLikesComment(0L);
        comment.setCreateTime(LocalDateTime.now());
        comment.setUser(user);

        commentRepository.save(comment);

        article.getCommentList().add(comment);
        articleRepository.save(article);
    }
    @Override
    public void updateComment(Long commentId, CommentRequest commentRequest) {
        String commentText = commentRequest.getText();
        Optional<Comment> optionalComment = commentRepository.findById(commentId);
        if (optionalComment.isPresent()) {
            Comment comment = optionalComment.get();
            comment.setCommentText(commentText);
            commentRepository.save(comment);
        }
    }
    @Override
    public void deleteComment(Long commentId) {
        commentRepository.deleteById(commentId);
    }
}
