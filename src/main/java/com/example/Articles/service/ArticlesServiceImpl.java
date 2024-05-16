package com.example.Articles.service;

import com.example.Articles.Config.UrlConfig;
import com.example.Articles.dto.request.ArticlesRequest;
import com.example.Articles.dto.response.ArticlesResponse;
import com.example.Articles.dto.response.ArticlesUrlResponse;
import com.example.Articles.model.Articles;
import com.example.Articles.repository.ArticlesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ArticlesServiceImpl implements ArticlesService {
    private final CreateArticlesHashImpl сreateArticlesHashImpl;
    private final ArticlesRepository articlesRepository;
    private final UrlConfig urlConfig;

    @Override
    public ArticlesResponse getByHash(String hash) {
        // Получаем объект Articles из репозитория по хешу
        Articles Articles = articlesRepository.getByHash(hash);

        // Проверяем, найден ли объект
        if (Articles == null) {
            return null; // или бросить исключение
        }
        // Преобразуем Articles в ArticlesResponse
        ArticlesResponse articlesResponse = new ArticlesResponse();
        articlesResponse.setName(Articles.getName());
        articlesResponse.setDescription(Articles.getDescription());
        articlesResponse.setPublic(Articles.isPublic());

        return articlesResponse;
    }

    @Override
    public List<ArticlesResponse> getFirstPublicArticles() {
        // Записываем первые 10 статей, которые isPublic и lifeTime - OK.
        List<Articles> list = articlesRepository.findAll();


        return list.stream().map(Articles ->
                        new ArticlesResponse(Articles.getName(), Articles.getDescription(), Articles.isPublic()))
                .collect(Collectors.toList());
    }

    @Override
    public ArticlesUrlResponse addAndResponseUrl(ArticlesRequest articlesRequest) {
        String hash = сreateArticlesHashImpl.createHashUrl(articlesRequest);

        Articles Articles = new Articles();
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
