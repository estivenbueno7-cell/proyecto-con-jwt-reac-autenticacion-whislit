package com.kevdev.wishlist.service;


import com.kevdev.wishlist.Entity.Product; // importamos entidad
import com.kevdev.wishlist.repository.ProductRepository;// inportamos repository
import org.springframework.stereotype.Service; // activamos el servce

import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> getAllProducts() {   // metodo de listar o buscar a todos
        return productRepository.findAll();
    }
       // BUSCAR PRODUCTO POR ID
    public Product getProductById(Long id) {

        return productRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Producto no encontrado con ID: " + id)
                );
    }

    // CREAR PRODUCTO
    public Product createProduct(Product product) {

        return productRepository.save(product);
    }

    // ACTUALIZAR PRODUCTO
    public Product updateProduct(Long id, Product product) {

        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Producto no encontrado con ID: " + id)
                );

        existingProduct.setName(product.getName());
        existingProduct.setDescription(product.getDescription());
        existingProduct.setPrice(product.getPrice());
        existingProduct.setStock(product.getStock());
        existingProduct.setActive(product.getActive());

        return productRepository.save(existingProduct);
    }

    // ELIMINAR PRODUCTO
    public void deleteProduct(Long id) {

        if (!productRepository.existsById(id)) {
            throw new RuntimeException(
                    "Producto no encontrado con ID: " + id
            );
        }

        productRepository.deleteById(id);
    }
}
