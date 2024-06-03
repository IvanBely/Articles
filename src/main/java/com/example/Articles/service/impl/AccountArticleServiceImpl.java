package com.example.Articles.service.impl;

import com.example.Articles.config.UrlConfig;
import com.example.Articles.dto.request.ArticleRequest;
import com.example.Articles.dto.response.ArticleResponse;
import com.example.Articles.dto.response.ArticleUrlResponse;
import com.example.Articles.model.Article;
import com.example.Articles.model.User;
import com.example.Articles.model.repository.ArticleRepository;
import com.example.Articles.service.AccountArticleService;
import com.example.Articles.service.CreateArticleHash;
import com.example.Articles.service.CreateTimeFormService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AccountArticleServiceImpl implements AccountArticleService {
    private final CreateArticleHash createArticleHash;
    private final ArticleRepository articleRepository;
    private final CreateTimeFormService createTimeFormService;
    private final UrlConfig urlConfig;



    @Override
    public List<ArticleResponse> getAccountArticle(User user) {

        List<Article> articleList = articleRepository.findAllByUserId(user.getId());

        return articleList.stream()
                .map(article ->
                        new ArticleResponse(article.getName(), article.getDescription(), article.getUser().getUsername(), createTimeFormService.createTimeForm(article)))
                .collect(Collectors.toList());
    }

    @Override
    public ArticleUrlResponse createArticleAndResponseUrl(User user, ArticleRequest articleRequest) {
        String hash = createArticleHash.createHashUrl();

        Article article = new Article();
        article.setUser(user);
        article.setHash(hash);
        article.setName(articleRequest.getName());
        article.setDescription(articleRequest.getDescription());
        article.setCreateTime(LocalDateTime.now()); // устанавливаем текущее время
        article.setLifeTime(articleRequest.getLifeTime());
        article.setPublic(articleRequest.isPublic());
        article.setLikesCount(0);
        articleRepository.save(article); // Сохраняем в базу данных

        return new ArticleUrlResponse(urlConfig.getHost() + "/" + article.getHash());
    }
}
