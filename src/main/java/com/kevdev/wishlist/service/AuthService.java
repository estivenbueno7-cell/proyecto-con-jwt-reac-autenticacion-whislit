package com.kevdev.wishlist.service;

import com.kevdev.wishlist.Entity.User;
import com.kevdev.wishlist.dto.LoginRequest;
import com.kevdev.wishlist.dto.LoginResponse;
import com.kevdev.wishlist.dto.RegisterRequest;
import com.kevdev.wishlist.repository.UserRepository;
import com.kevdev.wishlist.security.JwtService;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public LoginResponse login(
            LoginRequest request
    ) {

        User user = userRepository
                .findByEmail(request.email())
                .orElseThrow(() ->
                        new RuntimeException(
                                "Correo o contraseña incorrectos"
                        )
                );

        if (!user.getActive()) {
            throw new RuntimeException(
                    "El usuario está inactivo"
            );
        }

        if (!passwordEncoder.matches(
                request.password(),
                user.getPassword()
        )) {
            throw new RuntimeException(
                    "Correo o contraseña incorrectos"
            );
        }

        String token =
                jwtService.generateToken(
                        user.getId(),
                        user.getEmail()
                );

        return new LoginResponse(
                token,
                user.getId(),
                user.getName(),
                user.getEmail()
        );
    }

    public LoginResponse register(
            RegisterRequest request
    ) {

        if (userRepository
                .findByEmail(request.email())
                .isPresent()) {

            throw new RuntimeException(
                    "El correo ya está registrado"
            );
        }

        User user = User.builder()
                .name(request.name())
                .email(request.email())
                .password(
                        passwordEncoder.encode(
                                request.password()
                        )
                )
                .rol("CUSTOMER")
                .active(true)
                .build();

        User saved =
                userRepository.save(user);

        String token =
                jwtService.generateToken(
                        saved.getId(),
                        saved.getEmail()
                );

        return new LoginResponse(
                token,
                saved.getId(),
                saved.getName(),
                saved.getEmail()
        );
    }
}