package com.example.Articles.model.repository;

import com.example.Articles.model.Article;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {
    Optional<Article> findByHash(String hash);

    @Query("SELECT a FROM Article a WHERE a.isPublic = true " +
            "AND (CASE a.lifeTime " +
            "    WHEN 'DAY' THEN TIMESTAMPDIFF(DAY, a.createTime, CURRENT_TIMESTAMP) <= 1 " +
            "    WHEN 'WEEK' THEN TIMESTAMPDIFF(DAY, a.createTime, CURRENT_TIMESTAMP) <= 7 " +
            "    WHEN 'MONTH' THEN TIMESTAMPDIFF(MONTH, a.createTime, CURRENT_TIMESTAMP) <= 1 " +
            "    WHEN 'YEAR' THEN TIMESTAMPDIFF(YEAR, a.createTime, CURRENT_TIMESTAMP) <= 1 " +
            "    ELSE true " +
            "END) = true " +
            "ORDER BY a.id DESC")
    Page<Article> findPublicArticleWithValidLifeTime(Pageable pageable);

    List<Article> findAllByUserId(Long userId);
}