package com.example.Articles.controllers;

import com.example.Articles.dto.response.ArticlesResponse;
import com.example.Articles.service.impl.ArticlesServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class MainController {
    private final ArticlesServiceImpl articlesService;

    @GetMapping("/") // ToDo // всем
    public ResponseEntity<List<ArticlesResponse>> getPublicList() {
        List<ArticlesResponse> articlesResponseList = articlesService.getFirstPublicArticles();
        return ResponseEntity.ok(articlesResponseList);
    }
    @GetMapping("/{hash}") // ToDo // всем
    public ResponseEntity<ArticlesResponse> getByHash(@PathVariable String hash) {
        Optional<ArticlesResponse> optionalArticlesResponse = articlesService.getByHash(hash);
        if (optionalArticlesResponse.isPresent()) {
            return ResponseEntity.ok(optionalArticlesResponse.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
