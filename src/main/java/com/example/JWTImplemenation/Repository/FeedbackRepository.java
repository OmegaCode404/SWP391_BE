package com.example.JWTImplemenation.Repository;

import com.example.JWTImplemenation.Entities.Feedback;
import com.example.JWTImplemenation.Entities.User;
import com.example.JWTImplemenation.Entities.Watch;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FeedbackRepository extends JpaRepository<Feedback, Integer> {

    List<Feedback> findByBuyerAndWatchAndUser(User buyer, Watch watch, User user);
    List<Feedback> findByUser(User user);
    List<Feedback> findByWatchIdAndBuyerIdAndUserId(Integer watchId, Integer buyerId, Integer userId);
    List<Feedback> findByUserId(Integer userId);
}
