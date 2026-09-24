
package com.kevdev.wishlist.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record WishlistItemResponse(

        Long id,

        Long productId,

        String productName,

        BigDecimal price,

        Integer stock,

        Integer quantity,

        LocalDateTime addedAt,

        Boolean available,

        String message

) {}
