package com.example.Articles.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "comments")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "article_id")
    private Article article;
    private LocalDateTime createTime;
    private String commentText;
    private int likesComment;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

}
