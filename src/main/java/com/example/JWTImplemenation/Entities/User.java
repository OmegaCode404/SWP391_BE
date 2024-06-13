package com.example.JWTImplemenation.Entities;
import com.example.JWTImplemenation.Entities.Enum.Gender;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "_user")
public class User implements UserDetails {
    @Id
    @GeneratedValue
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
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Watch> watches;
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Cart cart;
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
    @Override
    public String getPassword() {
        return password;}
}
