package com.example.products.services;

import com.example.products.entities.Category;
import com.example.products.entities.Product;
import com.example.products.repositories.CategoryRepository;
import com.example.products.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    @Autowired
    public ProductService(ProductRepository productRepository, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    //getAll
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    //get by id
    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Producto no encontrado: " + id));
    }

    // post product
    public Product saveProduct(Product product) {
        product.setId(null);
        product.setCategory(validateAndGetCategory(product));
        return productRepository.save(product);
    }

    // delete product
    public void deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            throw new NoSuchElementException("Producto no encontrado: " + id);
        }
        productRepository.deleteById(id);
    }

    // update product
    public Product updateProductById(Long id, Product product) {
        Product oldProduct = getProductById(id);
        Category category = validateAndGetCategory(product);

        oldProduct.setName(product.getName());
        oldProduct.setPrice(product.getPrice());
        oldProduct.setDescription(product.getDescription());
        oldProduct.setCategory(category);
        return productRepository.save(oldProduct);
    }

    // Valida los datos (usado al crear y al editar) y devuelve la categoría real de la BD
    private Category validateAndGetCategory(Product product) {
        if (product.getName() == null || product.getName().isBlank()) {
            throw new IllegalArgumentException("El nombre es obligatorio");
        }
        if (product.getPrice() == null || product.getPrice().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("El precio es obligatorio y no puede ser negativo");
        }
        if (product.getCategory() == null || product.getCategory().getId() == null) {
            throw new IllegalArgumentException("La categoría es obligatoria");
        }
        return categoryRepository.findById(product.getCategory().getId())
                .orElseThrow(() -> new IllegalArgumentException("La categoría no existe"));
    }

}
