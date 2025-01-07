package com.example.taskplanner;

import com.example.taskplanner.model.Task;
import com.example.taskplanner.model.User;
import com.example.taskplanner.service.TaskService;
import com.example.taskplanner.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class TaskPlannerApplication {

    private TaskService taskService;
    private UserService userService;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public TaskPlannerApplication(TaskService taskService, UserService userService, PasswordEncoder passwordEncoder) {
        this.taskService = taskService;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @Bean
    public CommandLineRunner demo() {
        return (args) -> {

            var admin = addUser("admin", "admin", "ADMIN");
            addUser("user", "heslo", "USER");

            var t1 = addTask("Task 1", "Description 1", admin, null);
            addTask("Task 2", "Description 2", admin, null);
            addTask("Task 3", "Description 3", admin, null);

            var t11 = addTask("Task 1.1", "Description 1.1", admin, t1);
            addTask("Task 1.2", "Description 1.2", admin, t1);
            addTask("Task 1.3", "Description 1.3", admin, t1);

            var t111 = addTask("Task 1.1.1", "Description 1.1.1", admin, t11);
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

    private Task addTask(String title, String description, User user, Task parentTask) {

        Task task = new Task();
        task.setTitle(title);
        task.setUser(user);
        task.setDescription(description);
        if (parentTask != null) {
            task.setParentTask(parentTask);
        }
        taskService.save(task);

        return task;

    }

    public static void main(String[] args) {
        SpringApplication.run(TaskPlannerApplication.class, args);
    }

}
