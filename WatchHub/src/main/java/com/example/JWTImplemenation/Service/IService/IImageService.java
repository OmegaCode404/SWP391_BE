package com.example.JWTImplemenation.Service.IService;

import org.springframework.web.multipart.MultipartFile;

public interface IImageService {
    public String uploadImage(MultipartFile imageFile);
    }
