package com.example.Articles.model.repository;

import com.example.Articles.model.Articles;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface ArticlesRepository extends JpaRepository<Articles, Long> {
    Optional<Articles> findByHash(String hash);

    @Query("SELECT a FROM Articles a WHERE a.isPublic = true " +
            "AND (CASE a.lifeTime " +
            "    WHEN 'DAY' THEN TIMESTAMPDIFF(DAY, a.createTime, CURRENT_TIMESTAMP) <= 1 " +
            "    WHEN 'WEEK' THEN TIMESTAMPDIFF(DAY, a.createTime, CURRENT_TIMESTAMP) <= 7 " +
            "    WHEN 'MONTH' THEN TIMESTAMPDIFF(MONTH, a.createTime, CURRENT_TIMESTAMP) <= 1 " +
            "    WHEN 'YEAR' THEN TIMESTAMPDIFF(YEAR, a.createTime, CURRENT_TIMESTAMP) <= 1 " +
            "    ELSE true " +
            "END) = true " +
            "ORDER BY a.id DESC")
    Page<Articles> findPublicArticlesWithValidLifeTime(Pageable pageable);

    List<Articles> findAllByUserId(Long userId);
}