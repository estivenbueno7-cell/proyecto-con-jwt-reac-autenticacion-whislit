package com.kevdev.wishlist.dto;

import com.kevdev.wishlist.Entity.WishlistHistoryAction;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record WishlistHistoryResponse(
        Long id,
        Long productId,
        String productName,
        BigDecimal price,
        WishlistHistoryAction action,
        Integer quantity,
        LocalDateTime createdAt
) {}