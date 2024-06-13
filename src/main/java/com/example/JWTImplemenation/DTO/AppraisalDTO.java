package com.example.JWTImplemenation.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppraisalDTO {
    private Integer id;
    private String comments;
    private Integer value;
    private String yearOfProduction;
    private String material;
    private String thickness;
    private String dial;
    private String movement;
    private String crystal;
    private String bracket;
    private String buckle;
    private Integer appraiserId;
    private Integer watchId;
}
