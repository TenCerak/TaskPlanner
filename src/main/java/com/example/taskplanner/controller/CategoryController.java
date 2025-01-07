package com.example.taskplanner.controller;

import com.example.taskplanner.model.Category;
import com.example.taskplanner.service.CategoryService;
import com.example.taskplanner.service.TaskService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/categories")
public class CategoryController {

    private final CategoryService categoryService;
    private final TaskService taskService;

    public CategoryController(CategoryService categoryService , TaskService taskService) {
        this.taskService = taskService;
        this.categoryService = categoryService;
    }

    @GetMapping
    public String viewCategories(Model model) {
        List<Category> categories = categoryService.findAll();
        model.addAttribute("categories", categories);
        return "pages/categories";
    }

    @PostMapping
    public String createCategory(@ModelAttribute Category category) {
        categoryService.save(category);
        return "redirect:/categories";
    }

    @PostMapping("/update/{id}")
    public String updateCategory(@PathVariable Long id, @ModelAttribute Category category) {
        category.setId(id);
        categoryService.save(category);
        return "redirect:/categories";
    }

    @PostMapping("/delete/{id}")
    public String deleteCategory(@PathVariable Long id) {
        var cat = categoryService.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid category Id:" + id));
        if(cat != null)
        {
            if(taskService.findByCategory(cat).isEmpty())
            {
                categoryService.deleteById(cat.getId());
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
        if(categoryService.findByName(name).isEmpty()) {
            categoryService.save(category);
        }
        return "redirect:/categories";
    }
}