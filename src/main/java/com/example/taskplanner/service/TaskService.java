package com.example.taskplanner.service;

import com.example.taskplanner.model.Category;
import com.example.taskplanner.model.Task;
import com.example.taskplanner.model.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface TaskService {

        List<Task> findAll();
        Task save(Task task);
        void deleteById(Long id);
        Optional<Task> findById(Long id);
        List<Task> findAllById(List<Long> taskIds);
        List<Task> findByCompletedAndUser(boolean completed, User user);
        List<Task> findByParentTask(Task parentTask);
        List<Task> findByCategoryAndUserAndCompleted(Category category, User user, boolean completed);
        List<Task> findTasksByTagNameAndUserAndCompleted(String tagName, User user, boolean completed);
        List<Task> findByCategory(Category category);
        List<Task> findTasksByTagName(String tagName);

}
