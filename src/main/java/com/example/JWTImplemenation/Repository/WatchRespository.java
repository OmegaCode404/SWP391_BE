package com.example.JWTImplemenation.Repository;
import com.example.JWTImplemenation.Entities.Watch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Timestamp;
import java.util.List;

public interface WatchRespository extends JpaRepository<Watch, Integer> {
    List<Watch> findByUserId(Integer userId);
    @Query("SELECT w FROM Watch w WHERE " +
            "(:name IS NULL OR LOWER(w.name) LIKE LOWER(CONCAT('%', :name, '%'))) AND " +
            "(:brand IS NULL OR LOWER(w.brand) LIKE LOWER(CONCAT('%', :brand, '%'))) AND " +
            "(:minPrice IS NULL OR w.price >= :minPrice) AND " +
            "(:maxPrice IS NULL OR w.price <= :maxPrice)")
    List<Watch> searchWatches(String name, String brand, Integer minPrice, Integer maxPrice);
    @Query("SELECT SUM(w.price) FROM Watch w WHERE w.user.id = :userId AND w.isPaid = true AND w.lastModifiedDate BETWEEN :startDate AND :endDate")
    Integer calculateRevenueBetweenDatesForUser(@Param("userId") Integer userId, @Param("startDate") Timestamp startDate, @Param("endDate") Timestamp endDate);
}

