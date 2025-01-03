package com.example.taskplanner;

import com.example.taskplanner.model.Task;
import com.example.taskplanner.model.User;
import com.example.taskplanner.repository.TaskRepository;
import com.example.taskplanner.repository.UserRepository;
import com.example.taskplanner.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class TaskPlannerApplication {

    private TaskRepository taskRepository;
    private UserService userService;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public TaskPlannerApplication(TaskRepository taskRepository, UserService userService, PasswordEncoder passwordEncoder) {
        this.taskRepository = taskRepository;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @Bean
    public CommandLineRunner demo() {
        return (args) -> {

            var admin = addUser("admin", "heslo", "ADMIN");
            addUser("user", "heslo", "USER");

            addTask("Task 1", "Description 1",admin);
            addTask("Task 2", "Description 2",admin);
            addTask("Task 3", "Description 3",admin);
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

    private void addTask(String title, String description,User user) {

            Task task = new Task();
            task.setTitle(title);
            task.setUser(user);
            task.setDescription(description);
            taskRepository.save(task);

    }

    public static void main(String[] args) {
        SpringApplication.run(TaskPlannerApplication.class, args);
    }

}
