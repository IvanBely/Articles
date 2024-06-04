package com.example.Articles.controllers;

import com.example.Articles.dto.response.ArticleResponse;
import com.example.Articles.service.ArticleService;
import com.example.Articles.service.MainPageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
@RestController
public class MainController {
    @Autowired
    private MainPageService mainPageService;
    @GetMapping("/")
    public ResponseEntity<List<ArticleResponse>> getPublicList() {
        List<ArticleResponse> ArticleResponseList = mainPageService.getFirstPublicArticle();
        return ResponseEntity.ok(ArticleResponseList);
    }
}
