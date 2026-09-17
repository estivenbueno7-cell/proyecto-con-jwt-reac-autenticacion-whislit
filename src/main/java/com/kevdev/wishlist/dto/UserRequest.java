
package com.kevdev.wishlist.dto;

public record UserRequest(

        String name,
        String email,
        String rol,
        Boolean active

) {
}