package com.example.taskplanner.controller;

import com.example.taskplanner.model.Tag;
import com.example.taskplanner.repository.TagRepository;
import com.example.taskplanner.repository.TaskRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/tags")
@Controller
public class TagController {

    private final TagRepository tagRepository;
    private final TaskRepository taskRepository;

    public TagController(TagRepository tagRepository, TaskRepository taskRepository) {
        this.tagRepository = tagRepository;
        this.taskRepository = taskRepository;
    }

    @GetMapping
    public String viewTags(Model model) {
        model.addAttribute("tags", tagRepository.findAll());
        return "pages/tags";
    }

    @PostMapping("/add")
    public String addTag(@RequestParam String name) {
        Tag tag = new Tag();
        tag.setName(name);
        if(tagRepository.findByName(name).isEmpty()) {
            tagRepository.save(tag);
        }
        return "redirect:/tags";
    }

    @PostMapping("/delete/{id}")
    public String deleteTag(@PathVariable Long id) {
        var tag = tagRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid tag Id:" + id));
        if(tag != null)
        {
            if(taskRepository.findTasksByTagName(tag.getName()).isEmpty())
            {
                tagRepository.delete(tag);
            }
            else {
                return "redirect:/tags?error=Tag is used in tasks";
            }
        }
        return "redirect:/tags";
    }
}