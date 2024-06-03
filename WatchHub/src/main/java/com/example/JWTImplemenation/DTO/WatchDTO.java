package com.example.JWTImplemenation.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WatchDTO {
    private Integer id;
    private String name;
    private String brand;
    private String description;
    private Integer price;
    private Date createdDate;
    private List<String> imageUrl;
}