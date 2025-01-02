package com.example.taskplanner.controller;

import com.example.taskplanner.service.UserService;
import org.springframework.stereotype.Controller;
import com.example.taskplanner.model.Tag;
import com.example.taskplanner.model.Task;
import com.example.taskplanner.model.User;
import com.example.taskplanner.repository.TagRepository;
import com.example.taskplanner.repository.TaskRepository;
import com.example.taskplanner.repository.UserRepository;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public String getAllTasks(Model model) {
        List<Task> tasks = taskRepository.findByParentTaskIsNull();
        model.addAttribute("tasks", tasks);
        return "task_list";
    }

    @GetMapping("/tasks/new")
    public String showCreateForm(Model model) {
        List<Task> mainTasks = taskRepository.findAll();
        Task task = new Task();
        model.addAttribute("task", task);
        model.addAttribute("mainTasks", mainTasks);
        return "task_create";
    }

    @PostMapping("/tasks")
    public String createTask(@ModelAttribute Task task) {
        task.setUser(userService.getCurrentUser());
        taskRepository.save(task);
        return "redirect:/tasks";
    }

    @PostMapping("/tasks/{id}/complete")
    public String completeTask(@PathVariable Long id) {
        Task task = taskRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid task ID"));
        task.setCompleted(true);
        taskRepository.save(task);
        return "redirect:/tasks";
    }


}

