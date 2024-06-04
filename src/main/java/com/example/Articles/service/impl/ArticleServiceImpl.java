package com.example.Articles.service.impl;

import com.example.Articles.config.UrlConfig;
import com.example.Articles.dto.request.ArticleRequest;
import com.example.Articles.dto.response.ArticleResponse;
import com.example.Articles.dto.response.ArticleUrlResponse;
import com.example.Articles.model.Article;
import com.example.Articles.model.Comment;
import com.example.Articles.model.User;
import com.example.Articles.model.repository.ArticleRepository;
import com.example.Articles.model.repository.CommentRepository;
import com.example.Articles.service.ArticleService;
import com.example.Articles.service.CreateArticleHash;
import com.example.Articles.service.CreateTimeFormService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ArticleServiceImpl implements ArticleService {
    private final ArticleRepository articleRepository;
    private final UrlConfig urlConfig;
    private final CreateTimeFormService createTimeFormService;
    private final CreateArticleHash createArticleHash;
    private final CommentRepository commentRepository;
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

            String createTimeForm = createTimeFormService.createTimeForm(article.getCreateTime());
            articleResponse.setCreateTime(createTimeForm);

            return Optional.of(articleResponse);
        } else {
            return Optional.empty();
        }
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
        article.setPublic(articleRequest.getIsPublic());
        article.setLikesCount(0L);
        articleRepository.save(article); // Сохраняем в базу данных

        return new ArticleUrlResponse(urlConfig.getHost() + "/" + article.getHash());
    }

    @Override
    public void updateArticle(User user, ArticleRequest articleRequest, Article article) {

        if (articleRequest.getName() != null) {
            article.setName(articleRequest.getName());
        }
        if (articleRequest.getDescription() != null) {
            article.setDescription(articleRequest.getDescription());
        }
        if (articleRequest.getLifeTime() != null) {
            article.setLifeTime(articleRequest.getLifeTime());
        }
        if (articleRequest.getIsPublic() != null) {
            article.setPublic(articleRequest.getIsPublic());
        }

        // Сохранение в базу данных
        articleRepository.save(article);
    }

    @Override
    public void deleteArticle(User user, Article article) {
        List<Comment> commentList = article.getCommentList();
        for (Comment c : commentList) {
            commentRepository.delete(c);
        }
        articleRepository.delete(article);
    }
}
