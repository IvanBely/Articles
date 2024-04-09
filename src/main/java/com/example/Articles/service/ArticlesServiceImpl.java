package com.example.Articles.service;

import com.example.Articles.Config.UrlConfig;
import com.example.Articles.dto.request.ArticlesRequest;
import com.example.Articles.dto.response.ArticlesResponse;
import com.example.Articles.dto.response.ArticlesUrlResponse;
import com.example.Articles.model.ArticlesModel;
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
        // Получаем объект ArticlesModel из репозитория по хешу
        ArticlesModel articlesModel = articlesRepository.getByHash(hash);

        // Проверяем, найден ли объект
        if (articlesModel == null) {
            return null; // или бросить исключение
        }
        // Преобразуем ArticlesModel в ArticlesResponse
        ArticlesResponse articlesResponse = new ArticlesResponse();
        articlesResponse.setName(articlesModel.getName());
        articlesResponse.setDescription(articlesModel.getDescription());
        articlesResponse.setPublic(articlesModel.isPublic());

        return articlesResponse;
    }

    @Override
    public List<ArticlesResponse> getFirstPublicArticles() {
        // Записываем первые 10 статей, которые isPublic и lifeTime - OK.
        List<ArticlesModel> list = articlesRepository.findPublicArticlesWithValidLifeTime(urlConfig.getPublicListSize());

        return list.stream().map(articlesModel ->
                        new ArticlesResponse(articlesModel.getName(), articlesModel.getDescription(), articlesModel.isPublic()))
                .collect(Collectors.toList());
    }

    @Override
    public ArticlesUrlResponse addAndResponseUrl(ArticlesRequest articlesRequest) {
        String hash = сreateArticlesHashImpl.createHashUrl(articlesRequest);

        ArticlesModel articlesModel = new ArticlesModel();
        articlesModel.setHash(hash);
        articlesModel.setName(articlesRequest.getName());
        articlesModel.setDescription(articlesRequest.getDescription());
        articlesModel.setCreateTime(LocalDateTime.now()); // устанавливаем текущее время
        articlesModel.setLifeTime(articlesRequest.getLifeTime());
        articlesModel.setPublic(articlesRequest.isPublic());
        articlesRepository.save(articlesModel); // Сохраняем в базу данных

        return new ArticlesUrlResponse(urlConfig.getHost() + "/" + articlesModel.getHash());
    }
}
