package com.example.Articles.controllers;

import com.example.Articles.dto.request.CommentRequest;
import com.example.Articles.model.Article;
import com.example.Articles.model.Comment;
import com.example.Articles.model.User;
import com.example.Articles.model.repository.CommentRepository;
import com.example.Articles.model.repository.UserRepository;
import com.example.Articles.service.ArticleService;
import com.example.Articles.service.CommentService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.security.Principal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CommentControllerTest {

    @Mock
    private ArticleService articleService;

    @Mock
    private CommentService commentService;

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CommentController commentController;

    @Test
    public void addCommentToArticle_ReturnsOk_WhenArticleAndUserExist() {
        String hash = "hash";
        CommentRequest commentRequest = new CommentRequest();
        Principal principal = mock(Principal.class);
        when(principal.getName()).thenReturn("username");
        Article article = new Article();
        User user = new User();
        when(articleService.findByHash(hash)).thenReturn(Optional.of(article));
        when(userRepository.findByUsername("username")).thenReturn(Optional.of(user));

        ResponseEntity<String> responseEntity = commentController.addCommentToArticle(hash, commentRequest, principal);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("Comment added successfully.", responseEntity.getBody());
        verify(commentService, times(1)).addCommentToArticle(user, article, commentRequest);
    }

    @Test
    public void addCommentToArticle_ReturnsNotFound_WhenArticleOrUserNotFound() {
        String hash = "nonExistingHash";
        CommentRequest commentRequest = new CommentRequest();
        Principal principal = mock(Principal.class);
        when(principal.getName()).thenReturn("testUser");
        when(articleService.findByHash(hash)).thenReturn(Optional.empty());
        when(userRepository.findByUsername("testUser")).thenReturn(Optional.empty());

        ResponseEntity<String> responseEntity = commentController.addCommentToArticle(hash, commentRequest, principal);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    public void updateCommentInArticle_ReturnsOk_WhenCommentOwner() {
        String hash = "hash";
        Long commentId = 1L;
        CommentRequest commentRequest = new CommentRequest();
        Principal principal = mock(Principal.class);
        when(principal.getName()).thenReturn("username");
        Comment comment = new Comment();
        comment.setUser(new User());
        when(commentRepository.findById(commentId)).thenReturn(Optional.of(comment));
        when(userRepository.findByUsername("username")).thenReturn(Optional.of(comment.getUser()));

        ResponseEntity<String> responseEntity = commentController.updateCommentInArticle(hash, commentId, commentRequest, principal);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("Comment updated successfully.", responseEntity.getBody());
        verify(commentService, times(1)).updateComment(commentId, commentRequest);
    }

    @Test
    public void deleteCommentFromArticle_ReturnsOk_WhenCommentOwner() {
        Long commentId = 1L;
        Principal principal = mock(Principal.class);
        when(principal.getName()).thenReturn("username");
        Comment comment = new Comment();
        comment.setUser(new User());
        when(commentRepository.findById(commentId)).thenReturn(Optional.of(comment));
        when(userRepository.findByUsername("username")).thenReturn(Optional.of(comment.getUser()));

        ResponseEntity<String> responseEntity = commentController.deleteCommentFromArticle(commentId, principal);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("Comment deleted successfully.", responseEntity.getBody());
        verify(commentService, times(1)).deleteComment(commentId);
    }
}

