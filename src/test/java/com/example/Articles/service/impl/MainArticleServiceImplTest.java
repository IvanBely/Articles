package com.example.Articles.service.impl;

import com.example.Articles.config.UrlConfig;
import com.example.Articles.dto.response.ArticlesResponse;
import com.example.Articles.model.Articles;
import com.example.Articles.model.repository.ArticlesRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MainArticleServiceImplTest {

    @InjectMocks
    private MainArticleServiceImpl mainArticleService;

    @Mock
    private ArticlesRepository articlesRepository;

    @Mock
    private UrlConfig urlConfig;

    @Test
    public void testGetByHashFound() {
        String hash = "testHash";
        Articles article = new Articles();
        article.setName("Test Article");
        article.setDescription("Test Description");
        article.setPublic(true);
        when(articlesRepository.getByHash(hash)).thenReturn(Optional.of(article));

        Optional<ArticlesResponse> response = mainArticleService.getByHash(hash);
        assertTrue(response.isPresent());
        assertEquals("Test Article", response.get().getName());
        assertEquals("Test Description", response.get().getDescription());
    }

    @Test
    public void testGetByHashNotFound() {
        String hash = "nonExistentHash";
        when(articlesRepository.getByHash(hash)).thenReturn(Optional.empty());

        Optional<ArticlesResponse> response = mainArticleService.getByHash(hash);
        assertTrue(response.isEmpty());
    }

    @Test
    public void testGetByHashNotPublic() {
        String hash = "testHash";
        Articles article = new Articles();
        article.setName("Test Article");
        article.setDescription("Test Description");
        article.setPublic(false);
        when(articlesRepository.getByHash(hash)).thenReturn(Optional.of(article));

        Optional<ArticlesResponse> response = mainArticleService.getByHash(hash);
        assertTrue(response.isEmpty());
    }

    @Test
    public void testGetFirstPublicArticles() {
        Articles article = new Articles();
        article.setName("Test Article");
        article.setDescription("Test Description");
        when(urlConfig.getPublicListSize()).thenReturn(10);
        when(articlesRepository.findPublicArticlesWithValidLifeTime(any(Pageable.class)))
                .thenReturn(new PageImpl<>(Collections.singletonList(article)));

        List<ArticlesResponse> articlesResponses = mainArticleService.getFirstPublicArticles();
        assertEquals(1, articlesResponses.size());
        assertEquals("Test Article", articlesResponses.get(0).getName());
        assertEquals("Test Description", articlesResponses.get(0).getDescription());

        ArgumentCaptor<PageRequest> pageRequestCaptor = ArgumentCaptor.forClass(PageRequest.class);
        verify(articlesRepository).findPublicArticlesWithValidLifeTime(pageRequestCaptor.capture());
        assertEquals(0, pageRequestCaptor.getValue().getPageNumber());
        assertEquals(10, pageRequestCaptor.getValue().getPageSize());
    }
}
