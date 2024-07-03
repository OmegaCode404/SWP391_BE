package com.example.JWTImplemenation.Service;

import com.example.JWTImplemenation.DTO.FeedbackDTO;
import com.example.JWTImplemenation.Entities.Feedback;
import com.example.JWTImplemenation.Entities.User;
import com.example.JWTImplemenation.Entities.Watch;
import com.example.JWTImplemenation.Repository.FeedbackRepository;
import com.example.JWTImplemenation.Repository.UserRepository;
import com.example.JWTImplemenation.Repository.WatchRespository;
import com.example.JWTImplemenation.Service.IService.IFeedbackservice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class FeedbackService implements IFeedbackservice {

    @Autowired
    private FeedbackRepository feedbackRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private WatchRespository watchRepository;

    @Override
    public ResponseEntity<FeedbackDTO> addOrUpdateFeedback(FeedbackDTO feedbackDTO) {
        Optional<User> userOptional = userRepository.findById(feedbackDTO.getUserId());
        Optional<User> buyerOptional = userRepository.findById(feedbackDTO.getBuyerId());
        Optional<Watch> watchOptional = watchRepository.findById(feedbackDTO.getWatchId());

        if (userOptional.isPresent() && watchOptional.isPresent() && buyerOptional.isPresent()) {
            User user = userOptional.get();
            Watch watch = watchOptional.get();
            User buyer = buyerOptional.get();

            List<Feedback> existingFeedbacks = feedbackRepository.findByBuyerAndWatchAndUser(buyer, watch, user);

            if (!existingFeedbacks.isEmpty()) {
                Feedback existingFeedback = existingFeedbacks.get(0);
                long diffInMillies = Math.abs(System.currentTimeMillis() - existingFeedback.getCreatedDate().getTime());
                long diffInDays = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);

                if (diffInDays <= 7) {
                    existingFeedback.setComments(feedbackDTO.getComments());
                    existingFeedback.setRating(feedbackDTO.getRating());
                    feedbackRepository.save(existingFeedback);
                    updateUserRatings(user);
                    return ResponseEntity.ok(convertToDTO(existingFeedback));
                } else {
                    return ResponseEntity.badRequest().body(null);
                }
            } else {
                Feedback feedback = Feedback.builder()
                        .buyer(buyer)
                        .user(user)
                        .watch(watch)
                        .comments(feedbackDTO.getComments())
                        .rating(feedbackDTO.getRating())
                        .createdDate(new Timestamp(System.currentTimeMillis()))
                        .build();

                Feedback savedFeedback = feedbackRepository.save(feedback);
                updateUserRatings(user);
                return ResponseEntity.ok(convertToDTO(savedFeedback));
            }
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    public ResponseEntity<FeedbackDTO> getFeedbackByOrderItem(Integer watchId, Integer buyerId,Integer userId) {
        List<Feedback> feedbacks = feedbackRepository.findByWatchIdAndBuyerIdAndUserId(watchId, buyerId,userId);
        if (!feedbacks.isEmpty()) {
            return ResponseEntity.ok(convertToDTO(feedbacks.get(0))); // Return the first feedback found
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @Override
    public ResponseEntity<List<FeedbackDTO>> getFeedbackByUserId(Integer userId) {
        List<Feedback> feedbacks = feedbackRepository.findByUserId(userId);
        List<FeedbackDTO> feedbackDTOs = feedbacks.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(feedbackDTOs);
    }
    private void updateUserRatings(User user) {
        List<Feedback> feedbacks = feedbackRepository.findByUser(user);
        double averageRating = feedbacks.stream().mapToInt(Feedback::getRating).average().orElse(0.0);
        user.setRating(averageRating);
        userRepository.save(user);
    }

    private FeedbackDTO convertToDTO(Feedback feedback) {
        return FeedbackDTO.builder()
                .id(feedback.getId())
                .buyerId(feedback.getBuyer().getId())
                .userId(feedback.getUser().getId())
                .userName(feedback.getUser().getFirstName()+" " + feedback.getUser().getLastName())
                .avatarUrl(feedback.getUser().getAvatarUrl())
                .watchId(feedback.getWatch().getId())
                .watchName(feedback.getWatch().getName())
                .comments(feedback.getComments())
                .rating(feedback.getRating())
                .createdDate(feedback.getCreatedDate())
                .build();
    }
}
