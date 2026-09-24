
package com.kevdev.wishlist.repository;

import com.kevdev.wishlist.Entity.Wishlist;
import com.kevdev.wishlist.Entity.WishlistHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WishlistHistoryRepository
        extends JpaRepository<WishlistHistory, Long> {

    List<WishlistHistory> findByWishlistOrderByCreatedAtDesc(
            Wishlist wishlist
    );
}

