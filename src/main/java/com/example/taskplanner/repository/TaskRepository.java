package com.example.taskplanner.repository;

import com.example.taskplanner.model.Task;
import com.example.taskplanner.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task,Long> {
    List<Task> findByParentTask(Task parentTask);
    List<Task> findByCompleted(boolean completed);
    List<Task> findByParentTaskIsNull();
    @Query("SELECT t FROM Task t JOIN t.tags tag WHERE tag.name = :tagName")
    List<Task> findTasksByTagName(@Param("tagName") String tagName);
    List<Task> findByUser(User user);



}
