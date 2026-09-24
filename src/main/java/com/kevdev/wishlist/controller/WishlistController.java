
package com.kevdev.wishlist.controller;

import com.kevdev.wishlist.dto.WishlistItemRequest;
import com.kevdev.wishlist.dto.WishlistItemResponse;
import com.kevdev.wishlist.dto.WishlistItemUpdateRequest;
import com.kevdev.wishlist.service.WishlistService;
import com.kevdev.wishlist.dto.WishlistHistoryResponse;
import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/wishlists")
public class WishlistController {

    private final WishlistService wishlistService;

    public WishlistController(WishlistService wishlistService) {
        this.wishlistService = wishlistService;
    }

    // AGREGAR PRODUCTO A LA LISTA
    @PostMapping("/{userId}/items")
    public ResponseEntity<WishlistItemResponse> addProduct(
            @PathVariable Long userId,
            @Valid @RequestBody WishlistItemRequest request
    ) {

        WishlistItemResponse response =
                wishlistService.addProduct(userId, request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    // CONSULTAR LISTA DE DESEOS
    @GetMapping("/{userId}")
    public ResponseEntity<List<WishlistItemResponse>> getWishlist(
            @PathVariable Long userId
    ) {

        return ResponseEntity.ok(
                wishlistService.getWishlist(userId)
        );
    }

    // ELIMINAR PRODUCTO DE LA LISTA
    @DeleteMapping("/{userId}/items/{itemId}")
    public ResponseEntity<Void> removeProduct(
            @PathVariable Long userId,
            @PathVariable Long itemId
    ) {

        wishlistService.removeProduct(userId, itemId);

        return ResponseEntity.noContent().build();
    }

    // ACTUALIZAR CANTIDAD DEL PRODUCTO
    @PutMapping("/{userId}/items/{itemId}")
    public ResponseEntity<WishlistItemResponse> updateProduct(
            @PathVariable Long userId,
            @PathVariable Long itemId,
            @Valid @RequestBody WishlistItemUpdateRequest request
    ) {

        WishlistItemResponse response =
                wishlistService.updateProduct(
                        userId,
                        itemId,
                        request
                );

        return ResponseEntity.ok(response);
        }
        // CONSULTAR HISTORIAL DE LA LISTA DE DESEOS
@GetMapping("/{userId}/history")
public ResponseEntity<List<WishlistHistoryResponse>> getHistory(
        @PathVariable Long userId
) {
    return ResponseEntity.ok(
            wishlistService.getHistory(userId)
    );
}
}
