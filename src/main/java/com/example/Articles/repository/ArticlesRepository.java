package com.example.Articles.repository;

import com.example.Articles.model.Articles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ArticlesRepository extends JpaRepository<Articles, Long> {
    Articles getByHash(String hash);

    @Query("SELECT a FROM Articles a WHERE a.isPublic = true " +
            "AND (CASE a.lifeTime " +
            "    WHEN 'DAY' THEN TIMESTAMPDIFF(DAY, a.createTime, CURRENT_TIMESTAMP) <= 1 " +
            "    WHEN 'WEEK' THEN TIMESTAMPDIFF(DAY, a.createTime, CURRENT_TIMESTAMP) <= 7 " +
            "    WHEN 'MONTH' THEN TIMESTAMPDIFF(MONTH, a.createTime, CURRENT_TIMESTAMP) <= 1 " +
            "    WHEN 'YEAR' THEN TIMESTAMPDIFF(YEAR, a.createTime, CURRENT_TIMESTAMP) <= 1 " +
            "    ELSE true " +
            "END) = true " +
            "ORDER BY a.id DESC")
    List<Articles> findPublicArticlesWithValidLifeTime(@Param("count") int count); // поиск действующих по сроку годности
    // записей в размере int count
}
