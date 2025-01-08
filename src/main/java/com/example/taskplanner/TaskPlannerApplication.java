package com.example.taskplanner;

import com.example.taskplanner.model.Category;
import com.example.taskplanner.model.Tag;
import com.example.taskplanner.model.Task;
import com.example.taskplanner.model.User;
import com.example.taskplanner.service.CategoryService;
import com.example.taskplanner.service.TagService;
import com.example.taskplanner.service.TaskService;
import com.example.taskplanner.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class TaskPlannerApplication {

    private final TaskService taskService;
    private final UserService userService;
    private final TagService tagService;
    private final CategoryService categoryService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public TaskPlannerApplication(TaskService taskService, UserService userService, TagService tagService, CategoryService categoryService, PasswordEncoder passwordEncoder) {
        this.taskService = taskService;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.tagService = tagService;
        this.categoryService = categoryService;
    }

    @Bean
    public CommandLineRunner demo() {
        return (args) -> {

            var admin = addUser("admin", "admin", "ADMIN");
            addUser("user", "heslo", "USER");

            var category = new Category();
            category.setName("Category 1");
            categoryService.save(category);

            var tag = new Tag();
            tag.setName("Tag 1");
            tagService.save(tag);

            var tag2 = new Tag();
            tag2.setName("Tag 2");
            tagService.save(tag2);

            var t1 = addTask("Task 1", "Description 1", admin, null,category,null);
            t1.getTags().add(tag);
            t1.getTags().add(tag2);
            taskService.save(t1);
            addTask("Task 2", "Description 2", admin, null,null,null);
            addTask("Task 3", "Description 3", admin, null,category,null);

            var t11 = addTask("Task 1.1", "Description 1.1", admin, t1,null,null);
            t11.getTags().add(tag);
            taskService.save(t11);
            addTask("Task 1.2", "Description 1.2", admin, t1,null,null);
            addTask("Task 1.3", "Description 1.3", admin, t1,category,null);

            var t111 = addTask("Task 1.1.1", "Description 1.1.1", admin, t11,null,null);
        };
    }

    private User addUser(String username, String password, String role) {
        var user = userService.findByUsername(username);

        if (user == null) {
            user = new User();
            user.setUsername(username);
            user.setPassword(passwordEncoder.encode(password));
            user.setRole(role);
            userService.save(user);
        }
        return user;

    }

    private Task addTask(String title, String description, User user, Task parentTask, Category category, List<Tag> tags) {

        Task task = new Task();
        task.setTitle(title);
        task.setUser(user);
        task.setDescription(description);
        if (parentTask != null) {
            task.setParentTask(parentTask);
        }
        if (category != null) {
            task.setCategory(category);
        }
        if (tags != null) {
            task.setTags(tags);
        }
        taskService.save(task);

        return task;

    }

    public static void main(String[] args) {
        SpringApplication.run(TaskPlannerApplication.class, args);
    }

}
