package com.kevdev.wishlist.dto;

public record LoginResponse(
        String token,
        Long userId,
        String name,
        String email
) {}