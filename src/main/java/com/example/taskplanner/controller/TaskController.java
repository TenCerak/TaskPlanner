package com.example.taskplanner.controller;

import com.example.taskplanner.model.Category;
import com.example.taskplanner.model.Tag;
import com.example.taskplanner.model.Task;
import com.example.taskplanner.service.CategoryService;
import com.example.taskplanner.service.TagService;
import com.example.taskplanner.service.TaskService;
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

    private final TaskService taskService;
    private final UserService userService;
    private final TagService tagService;
    private final CategoryService categoryService;

    public TaskController(TaskService taskService, UserService userService, TagService tagService, CategoryService categoryService) {
        this.taskService = taskService;
        this.userService = userService;
        this.tagService = tagService;
        this.categoryService = categoryService;
    }

    @GetMapping
    public String viewTasks(Model model) {
        List<Task> tasks = taskService.findByCompletedAndUser(false,userService.getCurrentUser());
        tasks.sort(Comparator.comparing(Task::getDueDate, Comparator.nullsLast(Comparator.naturalOrder())));
        model.addAttribute("tasks", tasks);
        return "pages/tasks";
    }

    @PostMapping("/{id}/toggle-complete")
    public String toggleTaskCompletion(@PathVariable Long id) {
        Task task = taskService.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid task Id:" + id));
        if (!task.isCompleted()) {
            boolean allSubtasksCompleted = task.getSubTasks().stream().allMatch(Task::isCompleted);
            if (!allSubtasksCompleted) {
                return "redirect:/tasks/" + id + "?error=All subtasks must be completed first";
            }
        }
        task.setCompleted(!task.isCompleted());
        taskService.save(task);
        return "redirect:/tasks/" + id;
    }
    @PostMapping("/{id}/delete")
    public String deleteTask(@PathVariable Long id) {
        var task = taskService.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid task Id:" + id));
        var parentTask = task.getParentTask();

        deleteSubtasksRecursive(task);
        taskService.deleteById(task.getId());

        if (parentTask != null) {
            return "redirect:/tasks/" + parentTask.getId();
        }
        return "redirect:/tasks";
    }

    private void deleteSubtasksRecursive(Task task) {
        var subtasks = taskService.findByParentTask(task);
        for (var subtask : subtasks) {
            deleteSubtasksRecursive(subtask);
            taskService.deleteById(subtask.getId());
        }
    }

    @GetMapping("/new")
    public String showCreateTaskForm(@RequestParam(required = false) Long parentId, Model model) {
        Task task = new Task();
        if (parentId != null) {
            Task parentTask = taskService.findById(parentId).orElseThrow(() -> new IllegalArgumentException("Invalid parent task Id:" + parentId));
            task.setParentTask(parentTask);
        }
        model.addAttribute("task", task);
        model.addAttribute("categories", categoryService.findAll());
        return "pages/task_form";
    }

    @PostMapping
    public String createTask(@ModelAttribute Task task, ArrayList<Long> tagIds, Long parentTaskId, Long categoryId) {
        task.setUser(userService.getCurrentUser());
        if(tagIds != null) {
            List<Tag> tags = tagService.findAllById(tagIds);
            task.setTags(tags);
        }
        if (parentTaskId != null) {
            Task parentTask = taskService.findById(parentTaskId)
                    .orElseThrow(() -> new IllegalArgumentException("Invalid parent task Id:" + parentTaskId));
            task.setParentTask(parentTask);
        }
        if (categoryId != null) {
            task.setCategory(categoryService.findById(categoryId)
                    .orElseThrow(() -> new IllegalArgumentException("Invalid category Id:" + categoryId)));
        }
        taskService.save(task);
        if (task.getParentTask() != null) {
            return "redirect:/tasks/" + task.getParentTask().getId();
        }
        return "redirect:/tasks";
    }

    @GetMapping("/{id}")
    public String viewTask(@PathVariable Long id, Model model) {
        Task task = taskService.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid task Id:" + id));
        model.addAttribute("task", task);
        model.addAttribute("tags", tagService.findAll());
        return "pages/task_detail";
    }

    @PostMapping("/{id}/add-tag")
    public String addTagToTask(@PathVariable Long id, @RequestParam Long tagId) {
        Task task = taskService.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid task Id:" + id));
        Tag tag = tagService.findById(tagId).orElseThrow(() -> new IllegalArgumentException("Invalid tag Id:" + tagId));

        if(task.getTags().stream().anyMatch(t -> t.getId().equals(tagId)))
            return "redirect:/tasks/" + id;

        task.getTags().add(tag);
        taskService.save(task);
        return "redirect:/tasks/" + id;
    }

    @PostMapping("/{id}/remove-tag")
    public String removeTagFromTask(@PathVariable Long id, @RequestParam Long tagId) {
        Task task = taskService.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid task Id:" + id));
        Tag tag = tagService.findById(tagId).orElseThrow(() -> new IllegalArgumentException("Invalid tag Id:" + tagId));
        task.getTags().remove(tag);
        taskService.save(task);
        return "redirect:/tasks/" + id;
    }

    @GetMapping("/category/{categoryId}")
    public String viewTasksByCategory(@PathVariable Long categoryId, Model model) {
        Category category = categoryService.findById(categoryId).orElseThrow(() -> new IllegalArgumentException("Invalid category Id:" + categoryId));
        List<Task> tasks = taskService.findByCategoryAndUserAndCompleted(category, userService.getCurrentUser(),false);
        model.addAttribute("tasks", tasks);
        model.addAttribute("selectedCategory", category);
        return "pages/tasks";
    }

    @GetMapping("/completed")
    public String viewCompletedTasks(Model model) {
        List<Task> tasks = taskService.findByCompletedAndUser(true, userService.getCurrentUser());
        model.addAttribute("tasks", tasks);
        return "pages/tasks";
    }

    @GetMapping("/tag/{tagId}")
    public String viewTasksByTag(@PathVariable Long tagId, Model model) {
        Tag tag = tagService.findById(tagId).orElseThrow(() -> new IllegalArgumentException("Invalid tag Id:" + tagId));
        List<Task> tasks = taskService.findTasksByTagNameAndUserAndCompleted(tag.getName(), userService.getCurrentUser(), false);
        model.addAttribute("tasks", tasks);
        model.addAttribute("selectedTag", tag);
        return "pages/tasks";
    }
}