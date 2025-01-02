package com.example.taskplanner.controller;

import com.example.taskplanner.model.Tag;
import com.example.taskplanner.repository.TagRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/tags")
public class TagController {

    private final TagRepository tagRepository;

    public TagController(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    @GetMapping
    public String listTags(Model model) {
        List<Tag> tags = tagRepository.findAll();
        model.addAttribute("tags", tags);
        return "tag_list";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("tag", new Tag());
        return "tag_form";
    }

    @PostMapping
    public String createTag(@ModelAttribute Tag tag) {
        tagRepository.save(tag);
        return "redirect:/tags";
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model) {
        Tag tag = tagRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid tag ID"));
        model.addAttribute("tag", tag);
        return "tag_form";
    }

    @PostMapping("/{id}/edit")
    public String updateTag(@PathVariable Long id, @ModelAttribute Tag tag) {
        tag.setId(id);
        tagRepository.save(tag);
        return "redirect:/tags";
    }

    @PostMapping("/{id}/delete")
    public String deleteTag(@PathVariable Long id) {
        tagRepository.deleteById(id);
        return "redirect:/tags";
    }
}