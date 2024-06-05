package com.example.Articles.service.impl;

import com.example.Articles.config.UrlConfig;
import com.example.Articles.dto.response.ArticleResponse;
import com.example.Articles.dto.response.CommentResponse;
import com.example.Articles.model.Article;
import com.example.Articles.model.Comment;
import com.example.Articles.model.repository.ArticleRepository;
import com.example.Articles.model.repository.CommentRepository;
import com.example.Articles.service.CreateTimeFormService;
import com.example.Articles.service.MainPageService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MainPageServiceImpl implements MainPageService {
    private final UrlConfig urlConfig;
    private final ArticleRepository articleRepository;
    private final CreateTimeFormService createTimeFormService;
    private final CommentRepository commentRepository;

    @Override
    public List<ArticleResponse> getArticleForm(List<Article> articleList) {

        List<ArticleResponse> articleResponseList = new ArrayList<>();

        for (Article article : articleList) {
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

            articleResponseList.add(articleResponse);
        }

        return articleResponseList;
    }
    public List<ArticleResponse> getFirstPublicArticle() {
        int listSize = urlConfig.getPublicListSize();

        List<Article> articleList = articleRepository
                .findPublicArticleWithValidLifeTime(PageRequest.of(0, listSize)).getContent();

        List<ArticleResponse> articleResponseList = getArticleForm(articleList);
        return articleResponseList;
    }
}
