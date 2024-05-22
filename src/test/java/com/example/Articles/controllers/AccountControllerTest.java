package com.example.Articles.controllers;

import com.example.Articles.dto.request.ArticlesRequest;
import com.example.Articles.dto.response.ArticlesResponse;
import com.example.Articles.dto.response.ArticlesUrlResponse;
import com.example.Articles.model.User;
import com.example.Articles.model.repository.UserRepository;
import com.example.Articles.service.impl.AccountArticlesServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)
public class AccountControllerTest {

    @InjectMocks
    private AccountController accountController;

    @Mock
    private AccountArticlesServiceImpl accountArticlesService;

    @Mock
    private UserRepository userRepository;


    @Test
    public void testGetAccountArticles() {
        String accountName = "testAccount";
        User user = new User();
        user.setAccountName(accountName);

        ArticlesResponse articlesResponse = new ArticlesResponse("Test Article", "Test Description");
        List<ArticlesResponse> articlesResponses = Collections.singletonList(articlesResponse);

        when(userRepository.findByAccountName(accountName)).thenReturn(Optional.of(user));
        when(accountArticlesService.getAccountArticles(user)).thenReturn(articlesResponses);

        ResponseEntity<List<ArticlesResponse>> response = accountController.getAccountArticles(accountName);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(articlesResponses, response.getBody());
    }

    @Test
    public void testAddArticle() {
        String accountName = "testAccount";
        User user = new User();
        user.setAccountName(accountName);

        ArticlesRequest articlesRequest = new ArticlesRequest();
        articlesRequest.setName("Test Article");
        articlesRequest.setDescription("Test Description");

        ArticlesUrlResponse articlesUrlResponse = new ArticlesUrlResponse("http://localhost/testhash");

        when(userRepository.findByAccountName(accountName)).thenReturn(Optional.of(user));
        when(accountArticlesService.addAndResponseUrl(user, articlesRequest)).thenReturn(articlesUrlResponse);

        ResponseEntity<ArticlesUrlResponse> response = accountController.add(accountName, articlesRequest);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(articlesUrlResponse, response.getBody());
    }
}
