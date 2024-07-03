package com.example.JWTImplemenation.Controller;

import com.example.JWTImplemenation.Service.IService.IRevenueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/revenue")
public class RevenueController {

    @Autowired
    private IRevenueService revenueService;

    @GetMapping("/monthly/{year}/{userId}")
    public ResponseEntity<Map<String, Object>> getMonthlyRevenue(@PathVariable int year, @PathVariable Integer userId) {
        return ResponseEntity.ok(revenueService.calculateRevenueByMonth(year, userId));
    }

    @GetMapping("/yearly/{userId}")
    public ResponseEntity<Map<String, Object>> getYearlyRevenue(@PathVariable Integer userId) {
        return ResponseEntity.ok(revenueService.calculateRevenueByYear(userId));
    }

    @GetMapping("/range")
    public ResponseEntity<Map<String, Object>> getRevenueByDateRange(
            @RequestParam String startDate, @RequestParam String endDate, @RequestParam Integer userId) {
        return ResponseEntity.ok(revenueService.calculateRevenueByDateRange(startDate, endDate, userId));
    }
}
