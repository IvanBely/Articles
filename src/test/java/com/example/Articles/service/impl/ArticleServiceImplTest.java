package com.example.Articles.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.example.Articles.config.UrlConfig;
import com.example.Articles.dto.request.ArticleRequest;
import com.example.Articles.dto.response.ArticleUrlResponse;
import com.example.Articles.model.LifeTime;
import com.example.Articles.service.ArticleService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.Articles.dto.response.ArticleResponse;
import com.example.Articles.dto.response.CommentResponse;
import com.example.Articles.model.Article;
import com.example.Articles.model.Comment;
import com.example.Articles.model.User;
import com.example.Articles.model.repository.ArticleRepository;
import com.example.Articles.model.repository.CommentRepository;
import com.example.Articles.service.CreateTimeFormService;

@ExtendWith(MockitoExtension.class)
public class ArticleServiceImplTest {

    @Mock
    private ArticleRepository articleRepository;

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private CreateTimeFormServiceImpl createTimeFormService;
    @Mock
    private CreateArticleHashImpl createArticleHash;
    @Mock
    private UrlConfig urlConfig;

    @InjectMocks
    private ArticleServiceImpl articleService;

    @Test
    public void getArticleResponseByHash_ArticleFound_ReturnsArticleResponse() {
        String hash = "test-hash";
        Article article = new Article();
        article.setName("Test Article");
        article.setDescription("Test Description");
        article.setLikesCount(10L);
        User user = new User();
        user.setUsername("testUser");
        article.setUser(user);
        article.setCreateTime(LocalDateTime.now());
        Comment comment = new Comment();
        comment.setUser(user);
        comment.setCommentText("Test Comment");
        comment.setLikesComment(5L);
        List<Comment> comments = new ArrayList<>();
        comments.add(comment);
        when(articleRepository.findByHash(hash)).thenReturn(Optional.of(article));
        when(commentRepository.findAllByArticleId(article.getId())).thenReturn(comments);
        when(createTimeFormService.createTimeForm(any())).thenReturn("Formatted Time"); // устанавливаем ожидаемое значение

        Optional<ArticleResponse> result = articleService.getArticleResponseByHash(hash);

        assertTrue(result.isPresent());
        ArticleResponse articleResponse = result.get();
        assertEquals("Test Article", articleResponse.getName());
        assertEquals("Test Description", articleResponse.getDescription());
        assertEquals("testUser", articleResponse.getUsername());
        assertEquals("Formatted Time", articleResponse.getCreateTime());
        assertEquals(10L, articleResponse.getLikesCount());
        assertEquals(1, articleResponse.getCommentResponseList().size());
        CommentResponse commentResponse = articleResponse.getCommentResponseList().get(0);
        assertEquals("testUser", commentResponse.getUsername());
        assertEquals("Formatted Time", commentResponse.getCreateTime());
        assertEquals("Test Comment", commentResponse.getCommentText());
        assertEquals(5L, commentResponse.getLikesCount());
    }

    @Test
    public void getArticleResponseByHash_ArticleNotFound_ReturnsEmptyOptional() {
        String hash = "non-existent-hash";
        when(articleRepository.findByHash(hash)).thenReturn(Optional.empty());

        Optional<ArticleResponse> result = articleService.getArticleResponseByHash(hash);

        assertFalse(result.isPresent());
    }

    @Test
    public void createArticleAndResponseUrl_CreatesArticleAndReturnsUrlResponse() {
        User user = new User();
        user.setId(1L);
        ArticleRequest articleRequest = new ArticleRequest();
        articleRequest.setName("Test Article");
        articleRequest.setDescription("Test Description");
        articleRequest.setLifeTime(LifeTime.WEEK);
        articleRequest.setIsPublic(true);

        String hash = "test-hash";
        when(createArticleHash.createHashUrl()).thenReturn(hash);

        LocalDateTime currentTime = LocalDateTime.now();
        when(articleRepository.save(any())).thenAnswer(invocation -> {
            Article savedArticle = invocation.getArgument(0);
            savedArticle.setCreateTime(currentTime);
            return savedArticle;
        });

        ArticleUrlResponse response = articleService.createArticleAndResponseUrl(user, articleRequest);

        String expectedUrl = urlConfig.getHost() + "/" + hash;
        assertEquals(expectedUrl, response.getUrl());
        verify(articleRepository, times(1)).save(any());
    }

    @Test
    public void updateArticle_UpdatesArticleFields() {
        User user = new User();
        Article article = new Article();
        ArticleRequest articleRequest = new ArticleRequest();
        articleRequest.setName("Updated Name");
        articleRequest.setDescription("Updated Description");
        articleRequest.setLifeTime(LifeTime.MONTH);
        articleRequest.setIsPublic(false);

        articleService.updateArticle(user, articleRequest, article);

        assertEquals("Updated Name", article.getName());
        assertEquals("Updated Description", article.getDescription());
        assertEquals(LifeTime.MONTH, article.getLifeTime());
        assertFalse(article.isPublic());
        verify(articleRepository, times(1)).save(article);
    }

    @Test
    public void deleteArticle_DeletesArticleAndRelatedEntities() {
        User user = new User();
        Article article = new Article();
        article.setId(1L);

        List<Comment> commentList = new ArrayList<>();
        Comment comment1 = new Comment();
        commentList.add(comment1);
        Comment comment2 = new Comment();
        commentList.add(comment2);

        article.setCommentList(commentList);

        doNothing().when(commentRepository).delete(any(Comment.class));

        articleService.deleteArticle(user, article);

        verify(commentRepository, times(commentList.size())).delete(any(Comment.class));
        verify(articleRepository, times(1)).delete(article);
    }
}
