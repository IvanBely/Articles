package com.example.Articles.dto.request;

import com.example.Articles.model.LifeTime;
import lombok.Data;
@Data
public class ArticleRequest {
    private String name;
    private String description;
    private LifeTime lifeTime;
    private boolean isPublic;
}