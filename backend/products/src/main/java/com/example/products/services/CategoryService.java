package com.example.products.services;

import com.example.products.entities.Category;
import com.example.products.repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
        return categoryRepository.findById(id).orElse(null);
    }

    // add category

    public Category addCategory(Category category) {
        if (category.getName() == null) {
            throw new IllegalArgumentException("El nombre no puede ser null");
        }
        return categoryRepository.save(category);
    }

    // edit
    public Category updateCategory(Long id, Category category) {
        Category oldCategory = categoryRepository.findById(id).orElseThrow(() -> new RuntimeException("Categortia no encontrada: " + id));
        oldCategory.setName(category.getName());
        return categoryRepository.save(oldCategory);
    }

    // delete
    public void deleteCategory(Long id) {
        categoryRepository.deleteById(id);
    }
}
