package com.example.Articles.model.repository;

import com.example.Articles.model.Article;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ArticleRepositoryTest {

    @Mock
    private ArticleRepository articleRepository;

    @Test
    void testFindPublicArticleWithValidLifeTime() {
        // Создаем тестовые данные
        Article article = new Article();
        article.setId(1L);
        article.setName("Test Article");
        article.setDescription("This is a test article");
        article.setPublic(true);
        article.setCreateTime(LocalDateTime.now());

        // Создаем список статей
        Page<Article> articlesPage = new PageImpl<>(Collections.singletonList(article));

        // Мокируем поведение метода findPublicArticleWithValidLifeTime
        when(articleRepository.findPublicArticleWithValidLifeTime(any())).thenReturn(articlesPage);

        // Вызываем метод, который мы тестируем
        Page<Article> result = articleRepository.findPublicArticleWithValidLifeTime(PageRequest.of(0, 10));

        // Выводим информацию о статьях в консоль
        System.out.println("Articles:");
        result.getContent().forEach(a -> {
            System.out.println("ID: " + a.getId());
            System.out.println("Name: " + a.getName());
            System.out.println("Description: " + a.getDescription());
            System.out.println("Public: " + a.isPublic());
            System.out.println("Create Time: " + a.getCreateTime());
            System.out.println();
        });

    }
}
