package com.example.Articles.service.impl;

import com.example.Articles.config.UrlConfig;
import com.example.Articles.dto.response.ArticlesResponse;
import com.example.Articles.model.Articles;
import com.example.Articles.model.repository.ArticlesRepository;
import com.example.Articles.model.repository.UserRepository;
import com.example.Articles.service.MainArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
@Service
@RequiredArgsConstructor
public class MainArticleServiceImpl implements MainArticleService {
    private final ArticlesRepository articlesRepository;
    private final UserRepository userRepository;
    private final UrlConfig urlConfig;

    @Override
    public Optional<ArticlesResponse> getByHash(String hash) {
        // Получаем объект Articles из репозитория по хешу
        Optional<Articles> articlesOptional = articlesRepository.getByHash(hash);

        // Проверяем, найден ли объект. И публичный ли он.
        if (articlesOptional.isPresent() && articlesOptional.get().isPublic()) {
            Articles articles = articlesOptional.get();

            // Преобразуем Articles в ArticlesResponse
            ArticlesResponse articlesResponse = new ArticlesResponse();
            articlesResponse.setName(articles.getName());
            articlesResponse.setDescription(articles.getDescription());

            return Optional.of(articlesResponse);
        } else {
            return Optional.empty();
        }
    }

    @Override
    public List<ArticlesResponse> getFirstPublicArticles() {
        // Записываем первые 10 статей, которые isPublic и lifeTime - OK.
        int listSize = urlConfig.getPublicListSize();
        List<Articles> articlesList = articlesRepository
                .findPublicArticlesWithValidLifeTime(PageRequest.of(0, listSize)).getContent();


        return articlesList.stream().map(Articles ->
                        new ArticlesResponse(Articles.getName(), Articles.getDescription()))
                .collect(Collectors.toList());
    }
}
