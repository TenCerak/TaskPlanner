package com.example.taskplanner.repository;

import com.example.taskplanner.model.Category;
import com.example.taskplanner.model.Tag;
import com.example.taskplanner.model.Task;
import com.example.taskplanner.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task,Long> {
    List<Task> findByParentTask(Task parentTask);
    List<Task> findByCompleted(boolean completed);
    List<Task> findByCompletedAndUser(boolean completed, User user);
    List<Task> findByParentTaskIsNull();
    @Query("SELECT t FROM Task t JOIN t.tags tag WHERE tag.name = :tagName")
    List<Task> findTasksByTagName(@Param("tagName") String tagName);
    @Query("SELECT t FROM Task t JOIN t.tags tag WHERE tag.name = :tagName AND t.user = :user")
    List<Task> findTasksByTagNameAndUserAndCompleted(@Param("tagName") String tagName, User user, boolean completed);
    List<Task> findByUser(User user);
    List<Task> findByTagsContainingAndUser(Tag tag, User user);
    List<Task> findByCategory(Category category);
    List<Task> findByCategoryAndUser(Category category, User user);
    List<Task> findByCategoryAndUserAndCompleted(Category category,User user, boolean completed);





}
