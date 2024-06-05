package com.example.Articles.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ArticleResponse {
    private String name;
    private String description;
    private String username;
    private String createTime;
    private Long likesCount;
    private List<CommentResponse> commentResponseList;
}
