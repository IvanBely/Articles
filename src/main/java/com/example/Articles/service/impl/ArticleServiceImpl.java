package com.example.Articles.service.impl;

import com.example.Articles.config.UrlConfig;
import com.example.Articles.dto.request.ArticleRequest;
import com.example.Articles.dto.response.ArticleResponse;
import com.example.Articles.dto.response.ArticleUrlResponse;
import com.example.Articles.dto.response.CommentResponse;
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
import java.util.ArrayList;
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
        Optional<Article> articleOptional = articleRepository.findByHash(hash);

        if (articleOptional.isPresent()) {
            Article article = articleOptional.get();
            ArticleResponse articleResponse = new ArticleResponse();
            articleResponse.setName(article.getName());
            articleResponse.setDescription(article.getDescription());
            articleResponse.setUsername(article.getUser().getUsername());
            articleResponse.setCreateTime(createTimeFormService.createTimeForm(article.getCreateTime()));
            articleResponse.setLikesCount(article.getLikesCount());

            List<Comment> commentList = commentRepository.findAllByArticleId(article.getId());
            List<CommentResponse> commentResponseList = new ArrayList<>();

            for (Comment comment : commentList) {
                CommentResponse commentResponse = new CommentResponse();
                commentResponse.setUsername(comment.getUser().getUsername());
                commentResponse.setCreateTime(createTimeFormService.createTimeForm(comment.getCreateTime()));
                commentResponse.setCommentText(comment.getCommentText());
                commentResponse.setLikesCount(comment.getLikesComment());

                commentResponseList.add(commentResponse);
            }

            articleResponse.setCommentResponseList(commentResponseList);

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
        article.setCreateTime(LocalDateTime.now());
        article.setLifeTime(articleRequest.getLifeTime());
        article.setPublic(articleRequest.getIsPublic());
        article.setLikesCount(0L);
        articleRepository.save(article);

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
