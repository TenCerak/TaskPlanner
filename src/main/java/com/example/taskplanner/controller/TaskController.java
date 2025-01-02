package com.example.taskplanner.controller;

import com.example.taskplanner.service.UserService;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.stereotype.Controller;
import com.example.taskplanner.model.Tag;
import com.example.taskplanner.model.Task;
import com.example.taskplanner.repository.TagRepository;
import com.example.taskplanner.repository.TaskRepository;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class TaskController {

    private final TaskRepository taskRepository;
    private final UserService userService;
    private final TagRepository tagRepository;

    public TaskController(TaskRepository taskRepository, UserService userService, TagRepository tagRepository) {
        this.taskRepository = taskRepository;
        this.userService = userService;
        this.tagRepository = tagRepository;
    }

    @GetMapping("/tasks")
    public String getTasks(Model model) {
        List<Task> tasks = taskRepository.findAll();
        model.addAttribute("tasks", tasks);
        model.addAttribute("allSubTasksCompleted", tasks.stream()
                .collect(Collectors.toMap(Task::getId, this::areAllSubTasksCompleted)));
        return "task_list";
    }

    @GetMapping("/tasks/new")
    public String showCreateForm(Model model) {
        List<Task> mainTasks = taskRepository.findAll();
        List<Tag> allTags = tagRepository.findAll();
        Task task = new Task();
        model.addAttribute("task", task);
        model.addAttribute("mainTasks", mainTasks);
        model.addAttribute("allTags", allTags);
        return "task_create";
    }

    @PostMapping("/tasks")
    public String createTask(@ModelAttribute Task task, @RequestParam(value = "tags",required = false) List<Long> tagIds) {
        task.setUser(userService.getCurrentUser());

        if (tagIds != null) {
            List<Tag> tagList = tagRepository.findAllById(tagIds);
            task.setTags(tagList);
        }

        taskRepository.save(task);
        return "redirect:/tasks";
    }

    @PostMapping("/tasks/{id}/complete")
    public String completeTask(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        Task task = taskRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid task ID"));
        if (areAllSubTasksCompleted(task)) {
            task.setCompleted(true);
            taskRepository.save(task);
        } else {
            redirectAttributes.addFlashAttribute("error", "All subtasks must be completed before completing this task.");
        }
        return "redirect:/tasks";
    }

    private boolean areAllSubTasksCompleted(Task task) {
        for (Task subTask : task.getSubTasks()) {
            if (!subTask.isCompleted() || !areAllSubTasksCompleted(subTask)) {
                return false;
            }
        }
        return true;
    }

    @PostMapping("/tasks/{id}/delete")
    public String deleteTask(@PathVariable Long id) {
        taskRepository.deleteById(id);
        return "redirect:/tasks";
    }
}