package com.example.Articles.service.impl;

import com.example.Articles.config.UrlConfig;
import com.example.Articles.dto.response.ArticleResponse;
import com.example.Articles.model.Article;
import com.example.Articles.model.Comment;
import com.example.Articles.model.User;
import com.example.Articles.model.repository.ArticleRepository;
import com.example.Articles.model.repository.CommentRepository;
import com.example.Articles.model.repository.UserRepository;
import com.example.Articles.service.ArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
@Service
@RequiredArgsConstructor
public class ArticleServiceImpl implements ArticleService {
    private final ArticleRepository articleRepository;
    private final UserRepository userRepository;
    private final UrlConfig urlConfig;
    private final CommentRepository CommentRepository;
    @Override
    public Optional<Article> findByHash(String hash) {
        return articleRepository.findByHash(hash);
    }
    @Override
    public Optional<ArticleResponse> getFormByHash(String hash) {
        // Получаем объект Article из репозитория по хешу
        Optional<Article> articleOptional = articleRepository.findByHash(hash);

        // Проверяем, найден ли объект. И публичный ли он.
        if (articleOptional.isPresent() && articleOptional.get().isPublic()) {
            Article Article = articleOptional.get();

            // Преобразуем Article в ArticleResponse
            ArticleResponse articleResponse = new ArticleResponse();
            articleResponse.setName(Article.getName());
            articleResponse.setDescription(Article.getDescription());

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


        return articleList.stream().map(article ->
                        new ArticleResponse(article.getName(), article.getDescription()))
                .collect(Collectors.toList());
    }
}
