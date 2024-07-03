package com.example.JWTImplemenation.Service;

import com.example.JWTImplemenation.Repository.OrderRepository;
import com.example.JWTImplemenation.Repository.WatchRespository;
import com.example.JWTImplemenation.Service.IService.IRevenueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Service
public class RevenueService implements IRevenueService {

    @Autowired
    private WatchRespository watchRepository;

    @Override
    public Map<String, Object> calculateRevenueByMonth(int year, Integer userId) {
        Map<String, Object> revenueData = new HashMap<>();
        for (int month = 1; month <= 12; month++) {
            Timestamp start = Timestamp.valueOf(LocalDate.of(year, month, 1).atStartOfDay());
            Timestamp end = Timestamp.valueOf(LocalDate.of(year, month, start.toLocalDateTime().toLocalDate().lengthOfMonth()).atTime(23, 59, 59));
            Integer revenue = watchRepository.calculateRevenueBetweenDatesForUser(userId, start, end);
            revenueData.put("Month " + month, revenue == null ? 0 : revenue);
        }
        return revenueData;
    }

    @Override
    public Map<String, Object> calculateRevenueByYear(Integer userId) {
        Map<String, Object> revenueData = new HashMap<>();
        int currentYear = LocalDate.now().getYear();
        for (int year = 2020; year <= currentYear; year++) { // Adjust the starting year as needed
            Timestamp start = Timestamp.valueOf(LocalDate.of(year, 1, 1).atStartOfDay());
            Timestamp end = Timestamp.valueOf(LocalDate.of(year, 12, 31).atTime(23, 59, 59));
            Integer revenue = watchRepository.calculateRevenueBetweenDatesForUser(userId, start, end);
            revenueData.put("Year " + year, revenue == null ? 0 : revenue);
        }
        return revenueData;
    }

    @Override
    public Map<String, Object> calculateRevenueByDateRange(String startDate, String endDate, Integer userId) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate startLocalDate = LocalDate.parse(startDate, formatter);
        LocalDate endLocalDate = LocalDate.parse(endDate, formatter);
        Timestamp start = Timestamp.valueOf(startLocalDate.atStartOfDay());
        Timestamp end = Timestamp.valueOf(endLocalDate.atTime(23, 59, 59));

        long daysBetween = java.time.temporal.ChronoUnit.DAYS.between(startLocalDate, endLocalDate);

        Map<String, Object> revenueData = new HashMap<>();
        revenueData.put("Start Date", startDate);
        revenueData.put("End Date", endDate);

        if (daysBetween <= 365) {
            // Provide a monthly breakdown
            LocalDate current = startLocalDate;
            while (!current.isAfter(endLocalDate)) {
                LocalDate monthEnd = current.withDayOfMonth(current.lengthOfMonth());
                if (monthEnd.isAfter(endLocalDate)) {
                    monthEnd = endLocalDate;
                }
                Timestamp monthStartTimestamp = Timestamp.valueOf(current.atStartOfDay());
                Timestamp monthEndTimestamp = Timestamp.valueOf(monthEnd.atTime(23, 59, 59));
                Integer revenue = watchRepository.calculateRevenueBetweenDatesForUser(userId, monthStartTimestamp, monthEndTimestamp);
                revenueData.put("Month " + current.getMonthValue() + " " + current.getYear(), revenue == null ? 0 : revenue);
                current = monthEnd.plusDays(1);
            }
        } else {
            // Provide a yearly breakdown
            int startYear = startLocalDate.getYear();
            int endYear = endLocalDate.getYear();
            for (int year = startYear; year <= endYear; year++) {
                LocalDate yearStart = LocalDate.of(year, 1, 1);
                LocalDate yearEnd = LocalDate.of(year, 12, 31);
                if (yearStart.isBefore(startLocalDate)) {
                    yearStart = startLocalDate;
                }
                if (yearEnd.isAfter(endLocalDate)) {
                    yearEnd = endLocalDate;
                }
                Timestamp yearStartTimestamp = Timestamp.valueOf(yearStart.atStartOfDay());
                Timestamp yearEndTimestamp = Timestamp.valueOf(yearEnd.atTime(23, 59, 59));
                Integer revenue = watchRepository.calculateRevenueBetweenDatesForUser(userId, yearStartTimestamp, yearEndTimestamp);
                revenueData.put("Year " + year, revenue == null ? 0 : revenue);
            }
        }

        return revenueData;
    }

}