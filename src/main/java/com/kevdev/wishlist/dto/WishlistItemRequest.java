package com.kevdev.wishlist.dto;

import jakarta.validation.constraints.NotNull;

public record WishlistItemRequest(

        @NotNull(message = "El productId es obligatorio")
        Long productId

) {
}