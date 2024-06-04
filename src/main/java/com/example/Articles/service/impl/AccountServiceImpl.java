package com.example.Articles.service.impl;

import com.example.Articles.dto.response.ArticleResponse;
import com.example.Articles.dto.response.CommentResponse;
import com.example.Articles.dto.response.UserInfoResponse;
import com.example.Articles.model.Article;
import com.example.Articles.model.Comment;
import com.example.Articles.model.User;
import com.example.Articles.model.repository.ArticleRepository;
import com.example.Articles.model.repository.CommentRepository;
import com.example.Articles.model.repository.UserRepository;
import com.example.Articles.service.AccountService;
import com.example.Articles.service.CreateTimeFormService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {
    private final ArticleRepository articleRepository;
    private final CreateTimeFormService createTimeFormService;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;


    @Override
    public List<ArticleResponse> getAccountArticle(User user) {
        List<Article> articleList = articleRepository.findAllByUserId(user.getId());
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

    @Override
    public UserInfoResponse getUserInfo(User user) {
        UserInfoResponse userInfoResponse = new UserInfoResponse();
        userInfoResponse.setEmail(user.getEmail());
        userInfoResponse.setUsername(user.getUsername());
        userInfoResponse.setPassword("Your password");

        return userInfoResponse;
    }

    @Override
    public void setEmail(User user, String newEmail) {
        user.setEmail(newEmail);
    }

    @Override
    public void setUsername(User user, String newUsername) {
        user.setUsername(newUsername);
    }

    @Override
    public void setPassword(User user, String newPassword) {
        user.setPassword(newPassword);
    }

    @Override
    public void deleteAccount(User user) {
        List<Comment> commentList = commentRepository.findAllByUserId(user.getId());

        for (Comment c : commentList) {
            commentRepository.delete(c);
        }
        List<Article> articleList = articleRepository.findAllByUserId(user.getId());
        for (Article a : articleList) {
            articleRepository.delete(a);
        }
        userRepository.delete(user);
    }
}
