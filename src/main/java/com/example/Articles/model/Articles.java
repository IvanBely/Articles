package com.example.Articles.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table (name = "articles")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Articles {
    @Id
    @GeneratedValue (strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    @Column(columnDefinition = "TEXT") // Используем тип данных TEXT для поля description
    private String description;
    private String hash;
    private int likesCount;
    private LocalDateTime createTime;
    @Enumerated(EnumType.STRING)
    private LifeTime lifeTime;
    private boolean isPublic;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Users user;

    @OneToMany(mappedBy = "article")
    private List<Comments> commentsList;
}

