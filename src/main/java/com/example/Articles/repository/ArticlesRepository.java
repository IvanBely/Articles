package com.example.Articles.repository;

import com.example.Articles.model.ArticlesModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ArticlesRepository extends JpaRepository<ArticlesModel, Integer> {
    ArticlesModel getByHash(String hash);

    @Query("SELECT a FROM ArticlesModel a WHERE a.isPublic = true " +
            "AND (CASE a.lifeTime " +
            "    WHEN 'DAY' THEN FUNCTION('AGE', a.createTime, CURRENT_TIMESTAMP) <= 1 " +
            "    WHEN 'WEEK' THEN FUNCTION('AGE', a.createTime, CURRENT_TIMESTAMP) <= 7 " +
            "    WHEN 'MONTH' THEN FUNCTION('AGE', a.createTime, CURRENT_TIMESTAMP) <= 30 " +
            "    WHEN 'YEAR' THEN FUNCTION('AGE', a.createTime, CURRENT_TIMESTAMP) <= 365 " +
            "    ELSE true " +
            "END) = true " +
            "ORDER BY a.id DESC")
    List<ArticlesModel> findPublicArticlesWithValidLifeTime(int count);
}
