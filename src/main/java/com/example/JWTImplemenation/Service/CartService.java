package com.example.JWTImplemenation.Service;

import com.example.JWTImplemenation.DTO.CartDTO;
import com.example.JWTImplemenation.DTO.CartItemDTO;
import com.example.JWTImplemenation.Entities.CartItem;
import com.example.JWTImplemenation.Entities.Cart;
import com.example.JWTImplemenation.Entities.User;
import com.example.JWTImplemenation.Repository.CartItemRepository;
import com.example.JWTImplemenation.Repository.CartRepository;
import com.example.JWTImplemenation.Repository.UserRepository;
import com.example.JWTImplemenation.Service.IService.ICartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CartService implements ICartService {

    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CartItemRepository cartItemRepository;

    @Override
    public ResponseEntity<CartDTO> findCartByUserId(Integer userId) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isPresent()) {
            Optional<Cart> cart = cartRepository.findByUserId(userId);
            if (cart.isPresent()) {
                List<CartItem> cartItems = cart.get().getCartItems();
                double totalPrice = cartItems.stream().mapToDouble(CartItem::getPrice).sum();
                CartDTO cartDTO = new CartDTO();
                cartDTO.setCartItems(convertToDTOList(cartItems));
                cartDTO.setTotalPrice(totalPrice);
                return ResponseEntity.ok(cartDTO);
            }
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Override
    public ResponseEntity<CartItemDTO> addToCart(Integer userId, CartItem cartItem) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isPresent()) {
            Optional<Cart> optionalCart = cartRepository.findByUserId(userId);
            Cart cart;
            if (optionalCart.isPresent()) {
                cart = optionalCart.get();
            } else {
                cart = new Cart();
                cart.setUser(user.get());
                cart.setCartItems(new ArrayList<>());
            }
            cartItem.setCart(cart);
            cart.getCartItems().add(cartItem);
            CartItem savedCartItem = cartRepository.save(cart).getCartItems().stream()
                    .filter(item -> item.equals(cartItem))
                    .findFirst()
                    .orElseThrow();
            return ResponseEntity.ok(convertToDTO(savedCartItem));
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    @Override
    public ResponseEntity<Void> removeFromCart(Integer userId, Integer cartItemId) {
        Optional<CartItem> cartItem = cartItemRepository.findById(cartItemId);
        if (cartItem.isPresent() && cartItem.get().getCart().getUser().getId().equals(userId)) {
            cartItemRepository.delete(cartItem.get());
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    private CartItemDTO convertToDTO(CartItem cartItem) {
        CartItemDTO cartItemDTO = new CartItemDTO();
        cartItemDTO.setId(cartItem.getId());
        cartItemDTO.setName(cartItem.getWatch().getName());
        cartItemDTO.setBrand(cartItem.getWatch().getBrand());
        cartItemDTO.setPrice(cartItem.getWatch().getPrice() * cartItem.getQuantity());
        cartItemDTO.setImage(cartItem.getWatch().getImageUrl().get(0).getImageUrl());
        cartItemDTO.setQuantity(cartItem.getQuantity());
        return cartItemDTO;
    }

    private List<CartItemDTO> convertToDTOList(List<CartItem> cartItems) {
        return cartItems.stream().map(this::convertToDTO).collect(Collectors.toList());
    }
}
