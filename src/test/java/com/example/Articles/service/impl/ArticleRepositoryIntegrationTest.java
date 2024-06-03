package com.example.Articles.service.impl;

import com.example.Articles.model.Article;
import com.example.Articles.model.repository.ArticleRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.DirtiesContext;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ArticleRepositoryIntegrationTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ArticleRepository articleRepository;

    @Test
    public void testFindPublicArticleWithValidLifeTime() {
        // Создаем несколько статей и сохраняем их в базе данных
        createAndSaveArticles();

        // Получаем первую страницу статей с корректным временем жизни
        Pageable pageable = PageRequest.of(0, 10);
        Page<Article> articles = articleRepository.findPublicArticleWithValidLifeTime(pageable);

        // Проверяем, что вернулось ожидаемое количество статей
        assertEquals(10, articles.getContent().size());
    }

    private void createAndSaveArticles() {
        for (int i = 0; i < 15; i++) {
            Article article = new Article();
            article.setName("Article " + i);
            article.setPublic(true); // Устанавливаем статьи как публичные
            // Устанавливаем время создания статьи так, чтобы она была валидной согласно условиям вашего SQL-запроса
            // Например, для статьи с индексом i устанавливаем createTime равным текущему времени минус i дней
            article.setCreateTime(LocalDateTime.now().minusDays(i));
            entityManager.persistAndFlush(article);
        }
    }
}
