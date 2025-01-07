package com.example.taskplanner.service;

import com.example.taskplanner.model.Category;

import java.util.List;
import java.util.Optional;

public interface CategoryService {
    List<Category> findAll();
    Optional<Category> findById(Long id);
    Optional<Category> findByName(String name);
    Category save(Category category);
    void deleteById(Long id);
}
