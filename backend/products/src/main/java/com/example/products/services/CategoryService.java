package com.example.products.services;

import com.example.products.entities.Category;
import com.example.products.repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    //get all
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    // get by id
    public Category findById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Categoría no encontrada: " + id));
    }

    // add category
    public Category addCategory(Category category) {
        validateName(category);
        category.setId(null); // al crear, el id lo genera la BD
        return categoryRepository.save(category);
    }

    // edit
    public Category updateCategory(Long id, Category category) {
        Category oldCategory = findById(id);
        validateName(category);
        oldCategory.setName(category.getName());
        return categoryRepository.save(oldCategory);
    }

    // delete
    public void deleteCategory(Long id) {
        if (!categoryRepository.existsById(id)) {
            throw new NoSuchElementException("Categoría no encontrada: " + id);
        }
        categoryRepository.deleteById(id);
    }

    private void validateName(Category category) {
        if (category.getName() == null || category.getName().isBlank()) {
            throw new IllegalArgumentException("El nombre es obligatorio");
        }
    }
}
