package com.example.Articles.controllers;

import com.example.Articles.dto.request.ArticlesRequest;
import com.example.Articles.dto.response.ArticlesResponse;
import com.example.Articles.dto.response.ArticlesUrlResponse;
import com.example.Articles.service.ArticlesServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ArticlesController {
    private final ArticlesServiceImpl articlesService;
    @GetMapping("/")
    public List<ArticlesResponse> getPublicList() {
        return articlesService.getFirstPublicArticles();
    }
    @GetMapping("/{hash}")
    public ArticlesResponse getByHash(@PathVariable String hash) {
        return articlesService.getByHash(hash);
    }
    @PostMapping ("/")
    public ArticlesUrlResponse add(@RequestBody ArticlesRequest request) {
        return articlesService.addAndResponseUrl(request);
    }
}
