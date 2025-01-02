package com.example.taskplanner.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

import jakarta.validation.constraints.NotBlank;


@Entity
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    @NotBlank(message = "Tag name cannot be blank")
    private String name;

    @ManyToMany(mappedBy = "tags")
    private List<Task> tasks = new ArrayList<>();

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public List<Task> getTasks() { return tasks; }
    public void setTasks(List<Task> tasks) { this.tasks = tasks; }
}
