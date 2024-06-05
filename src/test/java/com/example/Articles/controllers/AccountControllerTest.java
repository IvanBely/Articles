package com.example.Articles.controllers;

import com.example.Articles.controllers.AccountController;
import java.security.Principal;
import com.example.Articles.dto.request.ArticleRequest;
import com.example.Articles.dto.response.ArticleResponse;
import com.example.Articles.dto.response.ArticleUrlResponse;
import com.example.Articles.model.Article;
import com.example.Articles.model.User;
import com.example.Articles.model.repository.ArticleRepository;
import com.example.Articles.model.repository.UserRepository;
import com.example.Articles.service.AccountService;
import com.example.Articles.service.ArticleService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AccountControllerTest {

    @Mock
    private AccountService accountService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ArticleRepository articleRepository;

    @Mock
    private ArticleService articleService;

    @InjectMocks
    private AccountController accountController;

    @Test
    public void getAccountArticle_ReturnsArticles_WhenUserExists() {
        String username = "testUser";
        User user = new User();
        user.setUsername(username);
        List<ArticleResponse> expectedArticles = new ArrayList<>();
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
        when(accountService.getAccountArticle(user)).thenReturn(expectedArticles);

        ResponseEntity<List<ArticleResponse>> responseEntity = accountController.getAccountArticle(username);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedArticles, responseEntity.getBody());
    }

    @Test
    public void getAccountArticle_ReturnsNotFound_WhenUserDoesNotExist() {
        String username = "nonExistentUser";
        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        ResponseEntity<List<ArticleResponse>> responseEntity = accountController.getAccountArticle(username);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    public void createArticle_ReturnsUrlResponse_WhenArticleCreatedSuccessfully() {
        String username = "testUser";
        User user = new User();
        user.setUsername(username);
        ArticleRequest articleRequest = new ArticleRequest();
        ArticleUrlResponse expectedResponse = new ArticleUrlResponse();
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
        when(articleService.createArticleAndResponseUrl(user, articleRequest)).thenReturn(expectedResponse);

        Principal principal = mock(Principal.class);
        when(principal.getName()).thenReturn(username);

        ResponseEntity<ArticleUrlResponse> responseEntity = accountController.createArticle(articleRequest, principal);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedResponse, responseEntity.getBody());
    }

    @Test
    public void updateArticle_ReturnsOk_WhenArticleAndUserExist() {
        String username = "testUser";
        Long articleId = 1L;
        User user = new User();
        user.setUsername(username);
        ArticleRequest articleRequest = new ArticleRequest();
        Article article = new Article();
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
        when(articleRepository.findById(articleId)).thenReturn(Optional.of(article));

        Principal principal = mock(Principal.class);
        when(principal.getName()).thenReturn(username);

        ResponseEntity<String> responseEntity = accountController.updateArticle(articleRequest, articleId, principal);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("Article update successfully.", responseEntity.getBody());
    }

    @Test
    public void updateArticle_ReturnsNotFound_WhenArticleOrUserDoesNotExist() {
        String username = "nonExistentUser";
        Long articleId = 1L;
        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());
        when(articleRepository.findById(articleId)).thenReturn(Optional.empty());

        Principal principal = mock(Principal.class);
        when(principal.getName()).thenReturn(username);
        ResponseEntity<String> responseEntity = accountController.updateArticle(new ArticleRequest(), articleId, principal);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    public void deleteArticle_ReturnsOk_WhenArticleAndUserExist() {
        String username = "testUser";
        Long articleId = 1L;
        User user = new User();
        user.setUsername(username);
        Article article = new Article();
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
        when(articleRepository.findById(articleId)).thenReturn(Optional.of(article));

        Principal principal = mock(Principal.class);
        when(principal.getName()).thenReturn(username);

        ResponseEntity<String> responseEntity = accountController.deleteArticle(articleId, principal);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("Article deleted successfully.", responseEntity.getBody());
    }

    @Test
    public void deleteArticle_ReturnsNotFound_WhenArticleOrUserDoesNotExist() {
        String username = "nonExistentUser";
        Long articleId = 1L;
        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());
        when(articleRepository.findById(articleId)).thenReturn(Optional.empty());

        Principal principal = mock(Principal.class);
        when(principal.getName()).thenReturn(username);

        ResponseEntity<String> responseEntity = accountController.deleteArticle(articleId, principal);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }
}
