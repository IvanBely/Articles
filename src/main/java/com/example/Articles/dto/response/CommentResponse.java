package com.example.Articles.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentResponse {
    private String username;
    private String createTime;
    private String commentText;
    private Long likesCount;

}
