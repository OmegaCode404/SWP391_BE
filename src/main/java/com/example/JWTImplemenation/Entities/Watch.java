package com.example.JWTImplemenation.Entities;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table
public class Watch {
    @Id
    @GeneratedValue
    private Integer id;
    private String name;
    private String brand;
    private Date createdDate;
    private String description;
    private Integer price;
//    private String yearOfProduction;
//    private String material;
//    private String thickness;
//    private String dial;
//    private String movement;
//    private String crystal;
//    private String bracket;
//    private String buckle;
    @OneToOne(mappedBy = "watch", cascade = CascadeType.ALL)
    private Appraisal appraisal;

    @OneToMany(mappedBy = "watch", cascade = CascadeType.ALL)
    private List<ImageUrl> imageUrl;
}

