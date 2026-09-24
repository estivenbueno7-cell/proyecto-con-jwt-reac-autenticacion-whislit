
package com.kevdev.wishlist.service;

import java.util.List;

import com.kevdev.wishlist.Entity.Product;
import com.kevdev.wishlist.Entity.User;
import com.kevdev.wishlist.Entity.Wishlist;
import com.kevdev.wishlist.Entity.WishlistHistory;
import com.kevdev.wishlist.Entity.WishlistHistoryAction;
import com.kevdev.wishlist.Entity.WishlistItem;

import com.kevdev.wishlist.dto.WishlistItemRequest;
import com.kevdev.wishlist.dto.WishlistItemResponse;
import com.kevdev.wishlist.dto.WishlistItemUpdateRequest;

import com.kevdev.wishlist.mapper.WishlistItemMapper;
import com.kevdev.wishlist.dto.WishlistHistoryResponse;
import com.kevdev.wishlist.repository.ProductRepository;
import com.kevdev.wishlist.repository.UserRepository;
import com.kevdev.wishlist.repository.WishlistHistoryRepository;
import com.kevdev.wishlist.repository.WishlistItemRepository;
import com.kevdev.wishlist.repository.WishlistRepository;

import org.springframework.stereotype.Service;

@Service
public class WishlistService {

    private final WishlistRepository wishlistRepository;
    private final WishlistItemRepository wishlistItemRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final WishlistItemMapper wishlistItemMapper;
    private final WishlistHistoryRepository wishlistHistoryRepository;

    public WishlistService(
            WishlistRepository wishlistRepository,
            WishlistItemRepository wishlistItemRepository,
            UserRepository userRepository,
            ProductRepository productRepository,
            WishlistItemMapper wishlistItemMapper,
            WishlistHistoryRepository wishlistHistoryRepository
    ) {
        this.wishlistRepository = wishlistRepository;
        this.wishlistItemRepository = wishlistItemRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.wishlistItemMapper = wishlistItemMapper;
        this.wishlistHistoryRepository = wishlistHistoryRepository;
    }

    // =========================================================
    // AGREGAR PRODUCTO A LA WISHLIST
    // =========================================================

    public WishlistItemResponse addProduct(
            Long userId,
            WishlistItemRequest request
    ) {

        // 1. Buscar usuario
        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Usuario no encontrado con ID: " + userId
                        )
                );

        // 2. Buscar wishlist
        // Si no existe, se crea automáticamente
        Wishlist wishlist = wishlistRepository.findByUser(user)
                .orElseGet(() -> {

                    Wishlist newWishlist = Wishlist.builder()
                            .user(user)
                            .build();

                    return wishlistRepository.save(newWishlist);
                });

        // 3. Buscar producto
        Product product = productRepository.findById(request.productId())
                .orElseThrow(() ->
                        new RuntimeException(
                                "Producto no encontrado con ID: "
                                        + request.productId()
                        )
                );

        // 4. Verificar producto activo
        if (product.getActive() == null || !product.getActive()) {

            throw new RuntimeException(
                    "El producto no está disponible"
            );
        }

        // 5. Verificar stock
        if (product.getStock() == null || product.getStock() <= 0) {

            throw new RuntimeException(
                    "El producto no tiene stock disponible"
            );
        }

        // 6. Verificar si ya existe
        boolean alreadyExists =
                wishlistItemRepository.existsByWishlistAndProduct(
                        wishlist,
                        product
                );

        if (alreadyExists) {

            throw new RuntimeException(
                    "El producto ya está en la lista de deseos"
            );
        }

        // 7. Crear WishlistItem
        WishlistItem wishlistItem = WishlistItem.builder()
                .wishlist(wishlist)
                .product(product)
                .build();

        // 8. Guardar
        WishlistItem savedItem =
                wishlistItemRepository.save(wishlistItem);

        // 9. Guardar historial
        WishlistHistory history = WishlistHistory.builder()
                .wishlist(wishlist)
                .product(product)
                .action(WishlistHistoryAction.ADDED)
                .quantity(savedItem.getQuantity())
                .build();

        wishlistHistoryRepository.save(history);

        // 10. Convertir Entity -> Response
        return wishlistItemMapper.toResponse(savedItem);
    }

    // =========================================================
    // CONSULTAR WISHLIST
    // =========================================================

    public List<WishlistItemResponse> getWishlist(Long userId) {

        // 1. Buscar usuario
        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Usuario no encontrado con ID: " + userId
                        )
                );

        // 2. Buscar wishlist
        Wishlist wishlist = wishlistRepository.findByUser(user)
                .orElse(null);

        // 3. Si no existe wishlist
        if (wishlist == null) {
            return List.of();
        }

        // 4. Buscar productos
        return wishlistItemRepository.findByWishlist(wishlist)
                .stream()
                .map(wishlistItemMapper::toResponse)
                .toList();
    }

    // =========================================================
    // ELIMINAR PRODUCTO DE LA WISHLIST
    // =========================================================

    public void removeProduct(
            Long userId,
            Long itemId
    ) {

        // 1. Buscar usuario
        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Usuario no encontrado con ID: " + userId
                        )
                );

        // 2. Buscar wishlist
        Wishlist wishlist = wishlistRepository.findByUser(user)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Lista de deseos no encontrada"
                        )
                );

        // 3. Buscar item
        WishlistItem item = wishlistItemRepository.findById(itemId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Producto de wishlist no encontrado"
                        )
                );

        // 4. Verificar pertenencia
        if (!item.getWishlist().getId().equals(wishlist.getId())) {

            throw new RuntimeException(
                    "Este producto no pertenece a la lista de este usuario"
            );
        }

        // 5. Guardar historial ANTES de eliminar
        WishlistHistory history = WishlistHistory.builder()
                .wishlist(wishlist)
                .product(item.getProduct())
                .action(WishlistHistoryAction.REMOVED)
                .quantity(item.getQuantity())
                .build();

        wishlistHistoryRepository.save(history);

        // 6. Eliminar producto
        wishlistItemRepository.delete(item);
    }

    // =========================================================
    // ACTUALIZAR CANTIDAD
    // =========================================================

    public WishlistItemResponse updateProduct(
            Long userId,
            Long itemId,
            WishlistItemUpdateRequest request
    ) {

        // 1. Buscar usuario
        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Usuario no encontrado con ID: " + userId
                        )
                );

        // 2. Buscar wishlist
        Wishlist wishlist = wishlistRepository.findByUser(user)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Lista de deseos no encontrada"
                        )
                );

        // 3. Buscar item
        WishlistItem item = wishlistItemRepository.findById(itemId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Producto de wishlist no encontrado"
                        )
                );

        // 4. Verificar pertenencia
        if (!item.getWishlist().getId().equals(wishlist.getId())) {

            throw new RuntimeException(
                    "Este producto no pertenece a la lista de este usuario"
            );
        }

        // 5. Obtener producto
        Product product = item.getProduct();

        // 6. Validar cantidad
        if (request.quantity() == null || request.quantity() <= 0) {

            throw new RuntimeException(
                    "La cantidad debe ser mayor que cero"
            );
        }

        // 7. Verificar stock
        if (product.getStock() == null || product.getStock() <= 0) {

            throw new RuntimeException(
                    "El producto ya no tiene stock disponible"
            );
        }

        // 8. Verificar cantidad contra stock
        if (request.quantity() > product.getStock()) {

            throw new RuntimeException(
                    "La cantidad solicitada supera el stock disponible"
            );
        }

        // 9. Actualizar cantidad
        item.setQuantity(request.quantity());

        // 10. Guardar cambios
        WishlistItem updatedItem =
                wishlistItemRepository.save(item);

        // 11. Guardar historial
        WishlistHistory history = WishlistHistory.builder()
                .wishlist(wishlist)
                .product(product)
                .action(WishlistHistoryAction.UPDATED)
                .quantity(updatedItem.getQuantity())
                .build();

        wishlistHistoryRepository.save(history);

        // 12. Convertir Entity -> Response
        return wishlistItemMapper.toResponse(updatedItem);
    }
    public List<WishlistHistoryResponse> getHistory(Long userId) {

    User user = userRepository.findById(userId)
            .orElseThrow(() ->
                    new RuntimeException(
                            "Usuario no encontrado con ID: " + userId
                    )
            );

    Wishlist wishlist = wishlistRepository.findByUser(user)
            .orElse(null);

    if (wishlist == null) {
        return List.of();
    }

    return wishlistHistoryRepository
            .findByWishlistOrderByCreatedAtDesc(wishlist)
            .stream()
            .map(history -> new WishlistHistoryResponse(
                    history.getId(),
                    history.getProduct().getId(),
                    history.getProduct().getName(),
                    history.getProduct().getPrice(),
                    history.getAction(),
                    history.getQuantity(),
                    history.getCreatedAt()
            ))
            .toList();
}
}

