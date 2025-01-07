package com.example.taskplanner.controller;

import com.example.taskplanner.model.Tag;
import com.example.taskplanner.model.Task;
import com.example.taskplanner.repository.TagRepository;
import com.example.taskplanner.repository.TaskRepository;
import com.example.taskplanner.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;


@Controller
@RequestMapping("/tasks")
public class TaskController {

    private final TaskRepository taskRepository;
    private final UserService userService;
    private final TagRepository tagRepository;

    public TaskController(TaskRepository taskRepository, UserService userService, TagRepository tagRepository) {
        this.taskRepository = taskRepository;
        this.userService = userService;
        this.tagRepository = tagRepository;
    }

    @GetMapping
    public String viewTasks(Model model) {
        List<Task> tasks = taskRepository.findByCompletedAndUser(false,userService.getCurrentUser());
        tasks.sort(Comparator.comparing(Task::getDueDate, Comparator.nullsLast(Comparator.naturalOrder())));
        model.addAttribute("tasks", tasks);
        return "pages/tasks";
    }

    @PostMapping("/{id}/toggle-complete")
    public String toggleTaskCompletion(@PathVariable Long id) {
        Task task = taskRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid task Id:" + id));
        if (!task.isCompleted()) {
            boolean allSubtasksCompleted = task.getSubTasks().stream().allMatch(Task::isCompleted);
            if (!allSubtasksCompleted) {
                return "redirect:/tasks/" + id + "?error=All subtasks must be completed first";
            }
        }
        task.setCompleted(!task.isCompleted());
        taskRepository.save(task);
        return "redirect:/tasks/" + id;
    }
    @PostMapping("/{id}/delete")
    public String deleteTask(@PathVariable Long id) {
        var task = taskRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid task Id:" + id));
        var parentTask = task.getParentTask();

        deleteSubtasksRecursive(task);
        taskRepository.delete(task);

        if (parentTask != null) {
            return "redirect:/tasks/" + parentTask.getId();
        }
        return "redirect:/tasks";
    }

    private void deleteSubtasksRecursive(Task task) {
        var subtasks = taskRepository.findByParentTask(task);
        for (var subtask : subtasks) {
            deleteSubtasksRecursive(subtask);
            taskRepository.delete(subtask);
        }
    }

    @GetMapping("/new")
    public String showCreateTaskForm(@RequestParam(required = false) Long parentId, Model model) {
        Task task = new Task();
        if (parentId != null) {
            Task parentTask = taskRepository.findById(parentId).orElseThrow(() -> new IllegalArgumentException("Invalid parent task Id:" + parentId));
            task.setParentTask(parentTask);
        }
        model.addAttribute("task", task);
        return "pages/task_form";
    }

    @PostMapping
    public String createTask(@ModelAttribute Task task, ArrayList<Long> tagIds, Long parentTaskId) {
        task.setUser(userService.getCurrentUser());
        if(tagIds != null) {
            List<Tag> tags = tagRepository.findAllById(tagIds);
            task.setTags(tags);
        }
        if (parentTaskId != null) {
            Task parentTask = taskRepository.findById(parentTaskId)
                    .orElseThrow(() -> new IllegalArgumentException("Invalid parent task Id:" + parentTaskId));
            task.setParentTask(parentTask);
        }
        taskRepository.save(task);
        if (task.getParentTask() != null) {
            return "redirect:/tasks/" + task.getParentTask().getId();
        }
        return "redirect:/tasks";
    }

    @GetMapping("/{id}")
    public String viewTask(@PathVariable Long id, Model model) {
        Task task = taskRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid task Id:" + id));
        model.addAttribute("task", task);
        model.addAttribute("tags", tagRepository.findAll());
        return "pages/task_detail";
    }

    @PostMapping("/{id}/add-tag")
    public String addTagToTask(@PathVariable Long id, @RequestParam Long tagId) {
        Task task = taskRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid task Id:" + id));
        Tag tag = tagRepository.findById(tagId).orElseThrow(() -> new IllegalArgumentException("Invalid tag Id:" + tagId));

        if(task.getTags().stream().anyMatch(t -> t.getId().equals(tagId)))
            return "redirect:/tasks/" + id;

        task.getTags().add(tag);
        taskRepository.save(task);
        return "redirect:/tasks/" + id;
    }

    @PostMapping("/{id}/remove-tag")
    public String removeTagFromTask(@PathVariable Long id, @RequestParam Long tagId) {
        Task task = taskRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid task Id:" + id));
        Tag tag = tagRepository.findById(tagId).orElseThrow(() -> new IllegalArgumentException("Invalid tag Id:" + tagId));
        task.getTags().remove(tag);
        taskRepository.save(task);
        return "redirect:/tasks/" + id;
    }
    @GetMapping("/completed")
    public String viewCompletedTasks(Model model) {
        List<Task> tasks = taskRepository.findByCompletedAndUser(true, userService.getCurrentUser());
        model.addAttribute("tasks", tasks);
        return "pages/tasks";
    }

}