package com.example.Articles.service.impl;

import com.example.Articles.dto.response.ArticleResponse;
import com.example.Articles.model.Article;
import com.example.Articles.model.Comment;
import com.example.Articles.model.User;
import com.example.Articles.model.repository.CommentRepository;
import com.example.Articles.service.CreateTimeFormService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)
public class MainPageServiceImplTest {

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private CreateTimeFormService createTimeFormService;

    @InjectMocks
    private MainPageServiceImpl mainPageService;

    @Test
    public void testGetArticleForm() {
        List<Article> articles = new ArrayList<>();
        for (int i = 1; i <= 3; i++) {
            Article article = new Article();
            article.setId((long) i);
            article.setName("Article " + i);
            article.setDescription("Description " + i);
            article.setUser(new User());
            article.setCreateTime(LocalDateTime.now());
            article.setLikesCount((long) (i * 10));
            articles.add(article);
        }

        when(commentRepository.findAllByArticleId(anyLong())).thenReturn(new ArrayList<>());

        List<ArticleResponse> articleResponses = mainPageService.getArticleForm(articles);

        assertEquals(3, articleResponses.size());
        for (int i = 0; i < 3; i++) {
            ArticleResponse response = articleResponses.get(i);
            assertEquals("Article " + (i + 1), response.getName());
            assertEquals("Description " + (i + 1), response.getDescription());
            assertEquals(0, response.getCommentResponseList().size());
        }
        verify(commentRepository, times(3)).findAllByArticleId(anyLong());
    }
}
