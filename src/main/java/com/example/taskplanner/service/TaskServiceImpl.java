package com.example.taskplanner.service;

import com.example.taskplanner.model.Category;
import com.example.taskplanner.model.Task;
import com.example.taskplanner.model.User;
import com.example.taskplanner.repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;

    public TaskServiceImpl(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public List<Task> findAll() {
        return taskRepository.findAll();
    }

    public Task save(Task task) {
        return taskRepository.save(task);
    }

    public void deleteById(Long id) {
        taskRepository.deleteById(id);
    }

    public Optional<Task> findById(Long id) {
        return taskRepository.findById(id);
    }

    public List<Task> findAllById(List<Long> taskIds) {
        return taskRepository.findAllById(taskIds);
    }

    public List<Task> findByCompletedAndUser(boolean completed, User user) {
        return taskRepository.findByCompletedAndUser(completed, user);
    }

    public List<Task> findByParentTask(Task parentTask) {
        return taskRepository.findByParentTask(parentTask);
    }

    public List<Task> findByCategoryAndUserAndCompleted(Category category, User user, boolean completed) {
        return taskRepository.findByCategoryAndUserAndCompleted(category, user, completed);
    }

    public List<Task> findTasksByTagNameAndUserAndCompleted(String tagName, User user, boolean completed) {
        return taskRepository.findTasksByTagNameAndUserAndCompleted(tagName, user, completed);
    }

    public List<Task> findByCategory(Category category) {
        return taskRepository.findByCategory(category);
    }

    public List<Task> findTasksByTagName(String tagName) {
        return taskRepository.findTasksByTagName(tagName);
    }
}
