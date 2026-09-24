
package com.kevdev.wishlist.mapper;

import com.kevdev.wishlist.Entity.WishlistItem;
import com.kevdev.wishlist.dto.WishlistItemResponse;
import org.springframework.stereotype.Component;

@Component
public class WishlistItemMapper {

    public WishlistItemResponse toResponse(WishlistItem item) {

        boolean available =
                item.getProduct().getStock() != null
                        && item.getProduct().getStock() > 0;

        String message;

        if (available) {
            message = "Producto disponible";
        } else {
            message = "El producto está agotado";
        }

        return new WishlistItemResponse(
                item.getId(),
                item.getProduct().getId(),
                item.getProduct().getName(),
                item.getProduct().getPrice(),
                item.getProduct().getStock(),
                item.getQuantity(),
                item.getAddedAt(),
                available,
                message
        );
    }
}

