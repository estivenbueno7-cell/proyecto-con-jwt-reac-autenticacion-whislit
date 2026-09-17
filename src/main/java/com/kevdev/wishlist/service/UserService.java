
package com.kevdev.wishlist.service;

import com.kevdev.wishlist.dto.UserRequest;
import com.kevdev.wishlist.dto.UserResponse;
import com.kevdev.wishlist.Entity.User;
import com.kevdev.wishlist.mapper.UserMapper;
import com.kevdev.wishlist.repository.UserRepository;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    // CONSTRUCTOR
    public UserService(
            UserRepository userRepository,
            UserMapper userMapper
    ) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    // LISTAR TODOS LOS USUARIOS
    public List<UserResponse> getAllUsers() {

        return userRepository.findAll()
                .stream()
                .map(userMapper::toResponse)
                .toList();
    }

    // BUSCAR USUARIO POR ID
    public UserResponse getUserById(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Usuario no encontrado con ID: " + id
                        )
                );

        return userMapper.toResponse(user);
    }

    // CREAR USUARIO
    public UserResponse createUser(UserRequest request) {

        if (userRepository.existsByEmail(request.email())) {
            throw new RuntimeException(
                    "Ya existe un usuario con ese correo"
            );
        }

        User user = userMapper.toEntity(request);

        User savedUser = userRepository.save(user);

        return userMapper.toResponse(savedUser);
    }

    // ACTUALIZAR USUARIO
    public UserResponse updateUser(
            Long id,
            UserRequest request
    ) {

        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Usuario no encontrado con ID: " + id
                        )
                );

        userRepository.findByEmail(request.email())
                .filter(existingUser ->
                        !existingUser.getId().equals(id)
                )
                .ifPresent(existingUser -> {
                    throw new RuntimeException(
                            "El correo ya pertenece a otro usuario"
                    );
                });

        user.setName(request.name());
        user.setEmail(request.email());
        user.setRol(request.rol());
        user.setActive(request.active());

        User updatedUser = userRepository.save(user);

        return userMapper.toResponse(updatedUser);
    }

    // ELIMINAR USUARIO
    public void deleteUser(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Usuario no encontrado con ID: " + id
                        )
                );

        userRepository.delete(user);
    }
}