package com.example.Articles.service.impl;

import com.example.Articles.config.UrlConfig;
import com.example.Articles.dto.response.ArticleResponse;
import com.example.Articles.model.Article;
import com.example.Articles.model.repository.ArticleRepository;
import com.example.Articles.service.ArticleService;
import com.example.Articles.service.CreateTimeFormService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ArticleServiceImpl implements ArticleService {
    private final ArticleRepository articleRepository;
    private final UrlConfig urlConfig;
    private final CreateTimeFormService createTimeFormService;
    @Override
    public Optional<Article> findByHash(String hash) {
        return articleRepository.findByHash(hash);
    }
    @Override
    public Optional<ArticleResponse> getArticleResponseByHash(String hash) {
        // Получаем объект Article из репозитория по хешу
        Optional<Article> articleOptional = articleRepository.findByHash(hash);

        // Проверяем, найден ли объект. И публичный ли он.
        if (articleOptional.isPresent() && articleOptional.get().isPublic()) {
            Article article = articleOptional.get();

            // Преобразуем Article в ArticleResponse
            ArticleResponse articleResponse = new ArticleResponse();
            articleResponse.setName(article.getName());
            articleResponse.setDescription(article.getDescription());
            articleResponse.setUsername(article.getUser().getUsername());

            String createTimeForm = createTimeFormService.createTimeForm(article);
            articleResponse.setCreateTime(createTimeForm);

            return Optional.of(articleResponse);
        } else {
            return Optional.empty();
        }
    }

    @Override
    public List<ArticleResponse> getFirstPublicArticle() {
        // Записываем первые listSize статей, которые isPublic и lifeTime - OK.
        int listSize = urlConfig.getPublicListSize();
        List<Article> articleList = articleRepository
                .findPublicArticleWithValidLifeTime(PageRequest.of(0, listSize)).getContent();

        return articleList.stream()
                .map(article ->
                        new ArticleResponse(article.getName(), article.getDescription(), article.getUser().getUsername(), createTimeFormService.createTimeForm(article)))
                .collect(Collectors.toList());
    }
}
