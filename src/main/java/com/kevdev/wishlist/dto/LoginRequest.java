package com.kevdev.wishlist.dto;

public record LoginRequest(
        String email,
        String password
) {}