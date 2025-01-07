package com.example.taskplanner.controller;

import com.example.taskplanner.model.Category;
import com.example.taskplanner.repository.CategoryRepository;
import com.example.taskplanner.repository.TaskRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/categories")
public class CategoryController {

    private final CategoryRepository categoryRepository;
    private final TaskRepository taskRepository;

    public CategoryController(CategoryRepository categoryRepository, TaskRepository taskRepository) {
        this.categoryRepository = categoryRepository;
        this.taskRepository = taskRepository;
    }

    @GetMapping
    public String viewCategories(Model model) {
        List<Category> categories = categoryRepository.findAll();
        model.addAttribute("categories", categories);
        return "pages/categories";
    }

    @PostMapping
    public String createCategory(@ModelAttribute Category category) {
        categoryRepository.save(category);
        return "redirect:/categories";
    }

    @PostMapping("/update/{id}")
    public String updateCategory(@PathVariable Long id, @ModelAttribute Category category) {
        category.setId(id);
        categoryRepository.save(category);
        return "redirect:/categories";
    }

    @PostMapping("/delete/{id}")
    public String deleteCategory(@PathVariable Long id) {
        var cat = categoryRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid category Id:" + id));
        if(cat != null)
        {
            if(taskRepository.findByCategory(cat).isEmpty())
            {
                categoryRepository.delete(cat);
            }
            else {
                return "redirect:/tags?error=Category is used in tasks";
            }
        }
        return "redirect:/categories";
    }

    @PostMapping("add")
    public String addCategory(@RequestParam String name) {
        Category category = new Category();
        category.setName(name);
        if(categoryRepository.findByName(name).isEmpty()) {
            categoryRepository.save(category);
        }
        return "redirect:/categories";
    }
}