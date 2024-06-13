package com.example.JWTImplemenation.Service.IService;

import com.example.JWTImplemenation.DTO.CartDTO;
import com.example.JWTImplemenation.DTO.CartItemDTO;
import com.example.JWTImplemenation.Entities.CartItem;
import org.springframework.http.ResponseEntity;

public interface ICartService {
    ResponseEntity<CartDTO> findCartByUserId(Integer userId);
    ResponseEntity<CartItemDTO> addToCart(Integer userId, CartItem cartItem);
    ResponseEntity<Void> removeFromCart(Integer userId, Integer cartItemId);

}
