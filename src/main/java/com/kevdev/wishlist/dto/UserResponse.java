
package com.kevdev.wishlist.dto;

public record UserResponse(

        Long id,
        String name,
        String email,
        String rol,
        Boolean active

) {
}