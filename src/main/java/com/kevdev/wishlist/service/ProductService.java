
package com.kevdev.wishlist.service;

import com.kevdev.wishlist.dto.ProductRequest;
import com.kevdev.wishlist.dto.ProductResponse;
import com.kevdev.wishlist.Entity.Product;
import com.kevdev.wishlist.mapper.ProductMapper;
import com.kevdev.wishlist.repository.ProductRepository;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    // CONSTRUCTOR
    public ProductService(
            ProductRepository productRepository,
            ProductMapper productMapper
    ) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
    }

    // ==========================================
    // LISTAR TODOS LOS PRODUCTOS
    // ==========================================
    public List<ProductResponse> getAllProducts() {

        return productRepository.findAll()
                .stream()
                .map(productMapper::toResponse)
                .toList();
    }

    // ==========================================
    // BUSCAR PRODUCTO POR ID
    // ==========================================
    public ProductResponse getProductById(Long id) {

        Product product = productRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Producto no encontrado con ID: " + id
                        )
                );

        return productMapper.toResponse(product);
    }

    // ==========================================
    // CREAR PRODUCTO
    // ==========================================
    public ProductResponse createProduct(ProductRequest request) {

        // Convertimos ProductRequest → Product
        Product product = productMapper.toEntity(request);

        // Guardamos en PostgreSQL
        Product savedProduct = productRepository.save(product);

        // Convertimos Product → ProductResponse
        return productMapper.toResponse(savedProduct);
    }

    // ==========================================
    // ACTUALIZAR PRODUCTO
    // ==========================================
    public ProductResponse updateProduct(
            Long id,
            ProductRequest request
    ) {

        // Buscamos el producto existente
        Product product = productRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Producto no encontrado con ID: " + id
                        )
                );

        // Actualizamos los datos
        product.setName(request.name());
        product.setDescription(request.description());
        product.setPrice(request.price());
        product.setStock(request.stock());
        product.setActive(request.active());

        // Guardamos los cambios
        Product updatedProduct = productRepository.save(product);

        // Devolvemos respuesta
        return productMapper.toResponse(updatedProduct);
    }

    // ==========================================
    // ELIMINAR PRODUCTO
    // ==========================================
    public void deleteProduct(Long id) {

        Product product = productRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Producto no encontrado con ID: " + id
                        )
                );

        productRepository.delete(product);
    }
}
