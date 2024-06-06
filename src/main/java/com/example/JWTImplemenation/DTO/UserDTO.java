package com.example.JWTImplemenation.DTO;

import com.example.JWTImplemenation.Entities.Enum.Gender;
import com.example.JWTImplemenation.Entities.Role;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDTO {
    private Integer id;
    private String password;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    @Enumerated(EnumType.STRING)
    private Gender gender;
    private String avatarUrl;
    private String address;
    private boolean status;
    @Enumerated(EnumType.STRING)
    private Role role;
}
