package com.example.auth.backend.dto;
public record LoginResponse(String accessToken,String tokenType,long expiresIn,String username,String role) {}
