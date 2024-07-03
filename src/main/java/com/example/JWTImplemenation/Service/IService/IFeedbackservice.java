package com.example.JWTImplemenation.Service.IService;

import com.example.JWTImplemenation.DTO.FeedbackDTO;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface IFeedbackservice {
    ResponseEntity<FeedbackDTO> addOrUpdateFeedback(FeedbackDTO feedbackDTO);

    ResponseEntity<FeedbackDTO> getFeedbackByOrderItem(Integer watchId, Integer buyerId,Integer userId);
    ResponseEntity<List<FeedbackDTO>> getFeedbackByUserId(Integer userId);
}
