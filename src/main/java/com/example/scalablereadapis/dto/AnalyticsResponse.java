package com.example.scalablereadapis.dto;

public record AnalyticsResponse(long totalPosts, long totalLikes, double averageLikes, long totalComments) { }
