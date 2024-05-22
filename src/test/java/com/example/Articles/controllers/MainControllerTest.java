package com.example.Articles.controllers;

import com.example.Articles.dto.response.ArticlesResponse;
import com.example.Articles.model.User;
import com.example.Articles.service.impl.AccountArticlesServiceImpl;
import com.example.Articles.service.impl.MainArticleServiceImpl;
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
import static org.mockito.Mockito.when;

import static org.mockito.Mockito.doNothing;
import static org.mockito.ArgumentMatchers.any;
@ExtendWith(MockitoExtension.class)
public class MainControllerTest {

    @InjectMocks
    private MainController mainController;

    @Mock
    private MainArticleServiceImpl mainArticleService;

    @Mock
    private AccountArticlesServiceImpl accountArticlesService;

    @Test
    public void testGetPublicList() {
        List<ArticlesResponse> expectedResponse = Collections.singletonList(new ArticlesResponse("Test Article", "Test Description"));
        when(mainArticleService.getFirstPublicArticles()).thenReturn(expectedResponse);

        ResponseEntity<List<ArticlesResponse>> responseEntity = mainController.getPublicList();
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedResponse, responseEntity.getBody());
    }

    @Test
    public void testGetByHashFound() {
        String hash = "testHash";
        ArticlesResponse expectedResponse = new ArticlesResponse("Test Article", "Test Description");
        when(mainArticleService.getByHash(hash)).thenReturn(Optional.of(expectedResponse));

        ResponseEntity<ArticlesResponse> responseEntity = mainController.getByHash(hash);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedResponse, responseEntity.getBody());
    }

    @Test
    public void testGetByHashNotFound() {
        String hash = "nonExistentHash";
        when(mainArticleService.getByHash(hash)).thenReturn(Optional.empty());

        ResponseEntity<ArticlesResponse> responseEntity = mainController.getByHash(hash);
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    public void testAddUser() {
        User user = new User();
        user.setId(1L);
        user.setAccountName("testName");
        user.setEmail("testEmail");
        user.setPassword("testPassword");

        doNothing().when(accountArticlesService).addUser(any(User.class));

        ResponseEntity<String> responseEntity = mainController.addUser(user);
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals("User is saved", responseEntity.getBody());
    }
}
