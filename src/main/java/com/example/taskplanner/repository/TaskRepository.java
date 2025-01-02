package com.example.taskplanner.repository;

import com.example.taskplanner.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task,Long> {
    List<Task> findByParentTask(Task parentTask);

    List<Task> findByCompleted(boolean completed);
    List<Task> findByParentTaskIsNull();



}
