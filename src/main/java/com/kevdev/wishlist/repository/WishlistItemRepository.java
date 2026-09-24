package com.kevdev.wishlist.repository;

import com.kevdev.wishlist.Entity.Product;
import com.kevdev.wishlist.Entity.Wishlist;
import com.kevdev.wishlist.Entity.WishlistItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface WishlistItemRepository extends JpaRepository<WishlistItem, Long> {

    List<WishlistItem> findByWishlist(Wishlist wishlist);

    Optional<WishlistItem> findByWishlistAndProduct(
            Wishlist wishlist,
            Product product
    );

    boolean existsByWishlistAndProduct(
            Wishlist wishlist,
            Product product
    );

    void deleteByWishlistAndProduct(
            Wishlist wishlist,
            Product product
    );
}