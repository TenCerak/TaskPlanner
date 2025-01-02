package com.example.taskplanner.controller;

import com.example.taskplanner.model.Task;
import com.example.taskplanner.model.User;
import com.example.taskplanner.repository.TaskRepository;
import com.example.taskplanner.repository.UserRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    public TaskController(TaskRepository taskRepository, UserRepository userRepository) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
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

