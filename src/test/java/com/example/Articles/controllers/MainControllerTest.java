package com.example.Articles.controllers;

import com.example.Articles.dto.response.ArticleResponse;
import com.example.Articles.service.MainPageService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MainControllerTest {
    @InjectMocks
    private MainController mainController;

    @Mock
    private MainPageService mainPageService;

    @Test
    public void getPublicList_ReturnsArticleResponseList() {
        List<ArticleResponse> articleResponseList = new ArrayList<>();
        when(mainPageService.getFirstPublicArticle()).thenReturn(articleResponseList);

        ResponseEntity<List<ArticleResponse>> responseEntity = mainController.getPublicList();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(articleResponseList, responseEntity.getBody());
    }
}
