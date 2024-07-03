package com.example.JWTImplemenation.Service.IService;

import java.util.Map;

public interface IRevenueService {
    Map<String, Object> calculateRevenueByMonth(int year, Integer userId);
    Map<String, Object> calculateRevenueByYear(Integer userId);
    Map<String, Object> calculateRevenueByDateRange(String startDate, String endDate, Integer userId);
}
