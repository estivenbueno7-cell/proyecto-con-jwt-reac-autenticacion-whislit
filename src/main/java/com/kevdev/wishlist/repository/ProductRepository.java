package com.kevdev.wishlist.repository;


import com.kevdev.wishlist.Entity.Product;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> { // Activamos jpa y tenemos el crud completo
}
