package com.example.taskplanner.controller;

import com.example.taskplanner.model.Tag;
import com.example.taskplanner.model.Task;
import com.example.taskplanner.model.User;
import com.example.taskplanner.repository.TagRepository;
import com.example.taskplanner.repository.TaskRepository;
import com.example.taskplanner.repository.UserRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final TagRepository tagRepository;

    public TaskController(TaskRepository taskRepository, UserRepository userRepository, TagRepository tagRepository) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
        this.tagRepository = tagRepository;
    }

    @PostMapping("/{taskId}/tags")
    public Task addTagToTask(@PathVariable Long taskId, @RequestBody Tag tag) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        if (tag.getName() == null || tag.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Tag name cannot be empty");
        }
        Tag existingTag = tagRepository.findByName(tag.getName().trim());
        if (existingTag == null) {
            existingTag = tagRepository.save(tag);
        }

        if (!task.getTags().contains(existingTag)) {
            task.getTags().add(existingTag);
        }
        return taskRepository.save(task);
    }

    @GetMapping("/{taskId}/tags")
    public List<Tag> getTagsForTask(@PathVariable Long taskId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        return task.getTags();
    }

    @GetMapping("/by-tag/{tagName}")
    public List<Task> getTasksByTag(@PathVariable String tagName) {
        List<Task> tasks = taskRepository.findTasksByTagName(tagName);
        if (tasks.isEmpty()) {
            throw new RuntimeException("No tasks found for tag: " + tagName);
        }
        return tasks;
    }

    @GetMapping
    public List<Task> getAllTasks() {
        return taskRepository.findByParentTaskIsNull();
    }

    @GetMapping("/completed")
    public List<Task> getCompletedTasks() {
        return taskRepository.findByCompleted(true);
    }

    @GetMapping("/incomplete")
    public List<Task> getIncompleteTasks() {
        return taskRepository.findByCompleted(false);
    }

    @GetMapping("/{id}")
    public Task getTask(@PathVariable Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found"));
    }

    @PostMapping
    public Task createTask(@RequestBody Task task, @RequestParam Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        task.setUser(user);
        return taskRepository.save(task);
    }

    @PutMapping("/{id}")
    public Task updateTask(@PathVariable Long id, @RequestBody Task taskDetails) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        task.setTitle(taskDetails.getTitle());
        task.setDescription(taskDetails.getDescription());
        task.setDueDate(taskDetails.getDueDate());
        task.setCompleted(taskDetails.isCompleted());

        return taskRepository.save(task);
    }

    @DeleteMapping("/{id}")
    public void deleteTask(@PathVariable Long id) {
        taskRepository.deleteById(id);
    }

    @PostMapping("/{parentId}/subtasks")
    public Task addSubTask(@PathVariable Long parentId, @RequestBody Task subTask) {
        Task parentTask = taskRepository.findById(parentId)
                .orElseThrow(() -> new RuntimeException("Parent task not found"));
        subTask.setParentTask(parentTask);
        return taskRepository.save(subTask);
    }

    @GetMapping("/{parentId}/subtasks")
    public List<Task> getSubTasks(@PathVariable Long parentId) {
        Task parentTask = taskRepository.findById(parentId)
                .orElseThrow(() -> new RuntimeException("Parent task not found"));
        return taskRepository.findByParentTask(parentTask);
    }


}

