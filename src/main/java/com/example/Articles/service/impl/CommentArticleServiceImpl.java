package com.example.Articles.service.impl;

import com.example.Articles.dto.request.ArticlesRequest;
import com.example.Articles.model.Articles;
import com.example.Articles.model.Comments;
import com.example.Articles.model.Users;
import com.example.Articles.model.repository.ArticlesRepository;
import com.example.Articles.model.repository.CommentsRepository;
import com.example.Articles.service.CommentArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentArticleServiceImpl implements CommentArticleService {
    private final CommentsRepository commentsRepository;
    private final ArticlesRepository articlesRepository;
    @Override
    public void addCommentToArticle(Users user, Articles article, String commentText) {
        // Создаем новый комментарий
        Comments comment = new Comments();
        comment.setArticle(article);
        comment.setCommentText(commentText);
        comment.setLikesComment(0); // Устанавливаем начальное значение лайков
        // Устанавливаем время создания комментария
        comment.setCreateTime(LocalDateTime.now());

        // Сохраняем комментарий
        commentsRepository.save(comment);

        // Добавляем комментарий к списку комментариев статьи
        article.getCommentsList().add(comment);
        // Обновляем статью в базе данных
        articlesRepository.save(article);
    }
    @Override
    public void updateComment(Long commentId, ArticlesRequest updatedComment) {
        // Получаем комментарий по его ID
        Optional<Comments> optionalComment = commentsRepository.findById(commentId);
        if (optionalComment.isPresent()) {
            Comments comment = optionalComment.get();
            // Обновляем текст комментария
            comment.setCommentText(updatedComment.getCommentText());
            // Сохраняем изменения в базе данных
            commentsRepository.save(comment);
        } else {
            // Если комментарий не найден, можно сгенерировать исключение или обработать иным способом
            // В данном примере просто выводим сообщение в консоль
            System.out.println("Comment not found with ID: " + commentId);
        }
    }
    @Override
    public void deleteComment(Long commentId) {
        // Удаляем комментарий по его ID
        commentsRepository.deleteById(commentId);
    }
}
