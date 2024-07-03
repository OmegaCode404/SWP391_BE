package com.example.JWTImplemenation.Controller;

import com.example.JWTImplemenation.DTO.FeedbackDTO;
import com.example.JWTImplemenation.Service.FeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/feedback")
public class FeedbackController {

    @Autowired
    private FeedbackService feedbackService;

    @PostMapping
    public ResponseEntity<FeedbackDTO> addOrUpdateFeedback(@RequestBody FeedbackDTO feedbackDTO) {
        return feedbackService.addOrUpdateFeedback(feedbackDTO);
    }

    @GetMapping("/{watchId}/{buyerId}/{userId}")
    public ResponseEntity<FeedbackDTO> getFeedbackByOrderItem(@PathVariable Integer watchId, @PathVariable Integer buyerId,@PathVariable Integer userId) {
        return feedbackService.getFeedbackByOrderItem(watchId, buyerId,userId);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<FeedbackDTO>> getFeedbackByUserId(@PathVariable Integer userId) {
        return feedbackService.getFeedbackByUserId(userId);
    }
}
