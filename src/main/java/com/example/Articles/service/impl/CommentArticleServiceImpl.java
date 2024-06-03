package com.example.Articles.service.impl;

import com.example.Articles.dto.request.ArticleRequest;
import com.example.Articles.model.Article;
import com.example.Articles.model.Comment;
import com.example.Articles.model.User;
import com.example.Articles.model.repository.ArticleRepository;
import com.example.Articles.model.repository.CommentRepository;
import com.example.Articles.service.CommentArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentArticleServiceImpl implements CommentArticleService {
    private final CommentRepository commentRepository;
    private final ArticleRepository articleRepository;
    @Override
    public void addCommentToArticle(User user, Article article, String commentText) {
        // Создаем новый комментарий
        Comment comment = new Comment();
        comment.setArticle(article);
        comment.setCommentText(commentText);
        comment.setLikesComment(0); // Устанавливаем начальное значение лайков
        // Устанавливаем время создания комментария
        comment.setCreateTime(LocalDateTime.now());

        // Сохраняем комментарий
        commentRepository.save(comment);

        // Добавляем комментарий к списку комментариев статьи
        article.getCommentList().add(comment);
        // Обновляем статью в базе данных
        articleRepository.save(article);
    }
    @Override
    public void updateComment(Long commentId, String newCommentText) {
        // Получаем комментарий по его ID
        Optional<Comment> optionalComment = commentRepository.findById(commentId);
        if (optionalComment.isPresent()) {
            Comment comment = optionalComment.get();
            // Обновляем текст комментария
            comment.setCommentText(newCommentText);
            // Сохраняем изменения в базе данных
            commentRepository.save(comment);
        }
    }
    @Override
    public void deleteComment(Long commentId) {
        commentRepository.deleteById(commentId);
    }
}
