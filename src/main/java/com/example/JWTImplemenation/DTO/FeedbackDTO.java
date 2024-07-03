package com.example.JWTImplemenation.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FeedbackDTO {
    private Integer id;
    private Integer buyerId;
    private Integer userId;
    private String userName;
    private String avatarUrl;
    private Integer watchId;
    private String watchName;
    private String comments;
    private Integer rating;
    private Timestamp createdDate;
}
