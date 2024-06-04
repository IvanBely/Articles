package com.example.Articles.dto.request;

import com.example.Articles.model.LifeTime;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ArticleRequest {
    private String name;
    private String description;
    private LifeTime lifeTime;
    @JsonProperty("isPublic")
    private Boolean isPublic;
}
