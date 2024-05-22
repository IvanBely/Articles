package com.example.Articles.service.impl;

import com.example.Articles.config.UrlConfig;
import com.example.Articles.dto.request.ArticlesRequest;
import com.example.Articles.dto.response.ArticlesResponse;
import com.example.Articles.dto.response.ArticlesUrlResponse;
import com.example.Articles.model.Articles;
import com.example.Articles.model.LifeTime;
import com.example.Articles.model.User;
import com.example.Articles.model.repository.ArticlesRepository;
import com.example.Articles.model.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)
public class AccountArticlesServiceImplTest {

    @InjectMocks
    private AccountArticlesServiceImpl accountArticlesService;

    @Mock
    private CreateArticlesHashImpl createArticlesHashImpl;

    @Mock
    private ArticlesRepository articlesRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UrlConfig urlConfig;

    @Mock
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    public void setUp() {

    }

    @Test
    public void testGetAccountArticles() {
        User user = new User();
        user.setId(1L);

        Articles article = new Articles();
        article.setName("Test Article");
        article.setDescription("Test Description");
        when(articlesRepository.findAllByUserId(user.getId())).thenReturn(Collections.singletonList(article));

        List<ArticlesResponse> articlesResponses = accountArticlesService.getAccountArticles(user);
        assertNotNull(articlesResponses);
        assertEquals(1, articlesResponses.size());
        assertEquals("Test Article", articlesResponses.get(0).getName());
        assertEquals("Test Description", articlesResponses.get(0).getDescription());
        // Output results to the console
        System.out.println("testGetAccountArticles - articlesResponses: " + articlesResponses);
    }

    @Test
    public void testAddUser() {
        User user = new User();
        user.setAccountName("testName");
        user.setEmail("testEmail");
        user.setPassword("testPassword");

        when(passwordEncoder.encode(user.getPassword())).thenReturn("encodedPassword");

        accountArticlesService.addUser(user);

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userCaptor.capture());

        User capturedUser = userCaptor.getValue();
        assertEquals("encodedPassword", capturedUser.getPassword());

    }

    @Test
    public void testAddAndResponseUrl() {
        User user = new User();
        ArticlesRequest request = new ArticlesRequest();
        request.setName("Test Article");
        request.setDescription("Test Description");
        request.setLifeTime(LifeTime.DAY);
        request.setPublic(true);

        String hash = "generatedHash";
        when(createArticlesHashImpl.createHashUrl()).thenReturn(hash);
        when(urlConfig.getHost()).thenReturn("http://localhost");

        ArticlesUrlResponse response = accountArticlesService.addAndResponseUrl(user, request);
        assertNotNull(response);
        assertEquals("http://localhost/generatedHash", response.getUrl());

        ArgumentCaptor<Articles> articlesCaptor = ArgumentCaptor.forClass(Articles.class);
        verify(articlesRepository).save(articlesCaptor.capture());

        Articles capturedArticles = articlesCaptor.getValue();
        assertEquals(user, capturedArticles.getUser());
        assertEquals(hash, capturedArticles.getHash());
        assertEquals("Test Article", capturedArticles.getName());
        assertEquals("Test Description", capturedArticles.getDescription());
        assertEquals(LifeTime.DAY, capturedArticles.getLifeTime());
        assertEquals(true, capturedArticles.isPublic());
        assertNotNull(capturedArticles.getCreateTime());
    }
}
