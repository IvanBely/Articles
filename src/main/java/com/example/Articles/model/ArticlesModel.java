package com.example.Articles.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
@Entity
@Table (name = "articles")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ArticlesModel {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private String name;
    @Column(columnDefinition = "TEXT") // Используем тип данных TEXT для поля description
    private String description;
    private String hash;
    private LocalDateTime createTime;
    @Enumerated(EnumType.STRING)
    private LifeTime lifeTime;
    private boolean isPublic;
}

