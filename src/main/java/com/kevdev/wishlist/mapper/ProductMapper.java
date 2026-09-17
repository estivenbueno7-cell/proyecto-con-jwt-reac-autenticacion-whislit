
package com.kevdev.wishlist.mapper;

import com.kevdev.wishlist.dto.ProductRequest;
import com.kevdev.wishlist.dto.ProductResponse;
import com.kevdev.wishlist.Entity.Product;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {

    // Convierte ProductRequest → Product
    public Product toEntity(ProductRequest request) {

        return Product.builder()
                .name(request.name())
                .description(request.description())
                .price(request.price())
                .stock(request.stock())
                .active(request.active())
                .build();
    }

    // Convierte Product → ProductResponse
    public ProductResponse toResponse(Product product) {

        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getStock(),
                product.getActive(),
                product.getCreatedAt(),
                product.getUpdatedAt()
        );
    }
}
