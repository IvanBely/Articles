package com.example.Articles.service.impl;

import com.example.Articles.Config.UrlConfig;
import com.example.Articles.dto.request.ArticlesRequest;
import com.example.Articles.dto.response.ArticlesResponse;
import com.example.Articles.dto.response.ArticlesUrlResponse;
import com.example.Articles.model.Articles;
import com.example.Articles.model.Users;
import com.example.Articles.model.repository.ArticlesRepository;
import com.example.Articles.model.repository.UsersRepository;
import com.example.Articles.service.ArticlesService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ArticlesServiceImpl implements ArticlesService {
    private final CreateArticlesHashImpl сreateArticlesHashImpl;
    private final ArticlesRepository articlesRepository;
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
        List<Articles> articlesList = articlesRepository.findPublicArticlesWithValidLifeTime(urlConfig.getPublicListSize());


        return articlesList.stream().map(Articles ->
                        new ArticlesResponse(Articles.getName(), Articles.getDescription()))
                .collect(Collectors.toList());
    }

    @Override
    public List<ArticlesResponse> getAccountArticles(Users user) {

        List<Articles> articlesList = articlesRepository.findAllByUserId(user.getId());

        return articlesList.stream().map(Articles ->
                        new ArticlesResponse(Articles.getName(), Articles.getDescription()))
                .collect(Collectors.toList());
    }

    @Override
    public ArticlesUrlResponse addAndResponseUrl(Users user, ArticlesRequest articlesRequest) {
        String hash = сreateArticlesHashImpl.createHashUrl(articlesRequest);

        Articles Articles = new Articles();
        Articles.setUser(user);
        Articles.setHash(hash);
        Articles.setName(articlesRequest.getName());
        Articles.setDescription(articlesRequest.getDescription());
        Articles.setCreateTime(LocalDateTime.now()); // устанавливаем текущее время
        Articles.setLifeTime(articlesRequest.getLifeTime());
        Articles.setPublic(articlesRequest.isPublic());
        articlesRepository.save(Articles); // Сохраняем в базу данных

        return new ArticlesUrlResponse(urlConfig.getHost() + "/" + Articles.getHash());
    }


}
