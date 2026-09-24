package com.kevdev.wishlist.dto;


import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record WishlistItemUpdateRequest(

        @NotNull(message = "La cantidad es obligatoria")
        @Positive(message = "La cantidad debe ser mayor que 0")
        Integer quantity

) {}