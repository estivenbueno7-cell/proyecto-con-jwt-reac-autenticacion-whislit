
package com.kevdev.wishlist.mapper;

import com.kevdev.wishlist.dto.UserRequest;
import com.kevdev.wishlist.dto.UserResponse;
import com.kevdev.wishlist.Entity.User;

import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    // DTO -> ENTIDAD
    public User toEntity(UserRequest request) {

        return User.builder()
                .name(request.name())
                .email(request.email())
                .rol(request.rol())
                .active(request.active())
                .build();
    }

    // ENTIDAD -> DTO
    public UserResponse toResponse(User user) {

        return new UserResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getRol(),
                user.getActive()
        );
    }
}