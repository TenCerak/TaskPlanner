package com.example.taskplanner.controller;

import com.example.taskplanner.model.Tag;
import com.example.taskplanner.repository.TaskRepository;
import com.example.taskplanner.service.TagService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/tags")
@Controller
public class TagController {

    private final TagService tagService;
    private final TaskRepository taskRepository;

    public TagController(TagService tagService, TaskRepository taskRepository) {
        this.tagService = tagService;
        this.taskRepository = taskRepository;
    }

    @GetMapping
    public String viewTags(Model model) {
        model.addAttribute("tags", tagService.findAll());
        return "pages/tags";
    }

    @PostMapping("/add")
    public String addTag(@RequestParam String name) {
        Tag tag = new Tag();
        tag.setName(name);
        if(tagService.findByName(name).isEmpty()) {
            tagService.save(tag);
        }
        return "redirect:/tags";
    }

    @PostMapping("/delete/{id}")
    public String deleteTag(@PathVariable Long id) {
        var tag = tagService.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid tag Id:" + id));
        if(tag != null)
        {
            if(taskRepository.findTasksByTagName(tag.getName()).isEmpty())
            {
                tagService.deleteById(tag.getId());
            }
            else {
                return "redirect:/tags?error=Tag is used in tasks";
            }
        }
        return "redirect:/tags";
    }
}