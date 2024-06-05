package com.example.Articles.controllers;

import com.example.Articles.dto.response.ArticleResponse;
import com.example.Articles.service.ArticleService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ArticleControllerTest {
    @Mock
    private ArticleService articleService;
    @InjectMocks
    private ArticleController articleController;

    @Test
    public void getByHash_ReturnsArticleResponse_WhenArticleExists() {
        String hash = "sampleHash";
        ArticleResponse articleResponse = new ArticleResponse();
        when(articleService.getArticleResponseByHash(hash)).thenReturn(Optional.of(articleResponse));

        ResponseEntity<ArticleResponse> responseEntity = articleController.getByHash(hash);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(articleResponse, responseEntity.getBody());
    }

    @Test
    public void getByHash_ReturnsNotFound_WhenArticleDoesNotExist() {
        String hash = "nonExistentHash";
        when(articleService.getArticleResponseByHash(hash)).thenReturn(Optional.empty());

        ResponseEntity<ArticleResponse> responseEntity = articleController.getByHash(hash);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }
}
