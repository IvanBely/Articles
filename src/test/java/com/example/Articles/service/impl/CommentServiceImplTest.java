package com.example.Articles.service.impl;

import com.example.Articles.dto.request.CommentRequest;
import com.example.Articles.model.Article;
import com.example.Articles.model.Comment;
import com.example.Articles.model.User;
import com.example.Articles.model.repository.ArticleRepository;
import com.example.Articles.model.repository.CommentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.AdditionalAnswers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CommentServiceImplTest {

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private ArticleRepository articleRepository;

    @InjectMocks
    private CommentServiceImpl commentService;

    @Test
    public void addCommentToArticle_AddsCommentToArticle() {
        User user = new User();
        user.setId(1L);
        user.setUsername("testUser");

        Article article = new Article();
        article.setId(1L);
        article.setUser(user);
        article.setCommentList(new ArrayList<>());

        CommentRequest commentRequest = new CommentRequest();
        commentRequest.setText("Test comment");

        Comment comment = new Comment();
        comment.setArticle(article);
        comment.setCommentText(commentRequest.getText());
        comment.setLikesComment(0L);
        comment.setCreateTime(LocalDateTime.now());
        comment.setUser(user);

        when(articleRepository.save(any(Article.class))).thenReturn(article);
        when(commentRepository.save(any(Comment.class))).then(AdditionalAnswers.returnsFirstArg());

        commentService.addCommentToArticle(user, article, commentRequest);

        assertEquals(1, article.getCommentList().size());
        verify(articleRepository, times(1)).save(article);
        verify(commentRepository, times(1)).save(any(Comment.class));
    }
    @Test
    public void updateComment_UpdatesCommentText() {
        Long commentId = 1L;
        String newText = "Updated comment text";
        CommentRequest commentRequest = new CommentRequest();
        commentRequest.setText(newText);

        Comment comment = new Comment();
        Optional<Comment> optionalComment = Optional.of(comment);

        when(commentRepository.findById(commentId)).thenReturn(optionalComment);
        when(commentRepository.save(any(Comment.class))).thenAnswer(invocation -> invocation.getArgument(0));

        commentService.updateComment(commentId, commentRequest);

        verify(commentRepository, times(1)).findById(commentId);
        verify(commentRepository, times(1)).save(any(Comment.class));

        assertEquals(newText, comment.getCommentText());
    }
    @Test
    public void deleteComment_DeletesCommentById() {
        Long commentId = 1L;

        commentService.deleteComment(commentId);

        verify(commentRepository, times(1)).deleteById(commentId);
    }
}
