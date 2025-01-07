@param java.util.List<com.example.taskplanner.model.Task> tasks
@param com.example.taskplanner.model.Category selectedCategory
@param com.example.taskplanner.model.Tag selectedTag
@template.layout(title="Task Planner - View Tasks", content=@`

    <h1 class="text-center my-4">View Tasks</h1>

    <div class="row">
        <a href="/tasks/new" class="btn btn-primary">Add Task</a>
        @if(selectedCategory != null)
            <h2 class="my-4">Tasks in category: ${selectedCategory.getName()}</h2>
        @endif
        @if(selectedTag != null)
            <h2 class="my-4">Tasks with tag: ${selectedTag.getName()}</h2>
        @endif
        @if(selectedCategory != null || selectedTag != null)
            <a href="/tasks" class="btn btn-secondary">Clear filters</a>
        @endif
        @for(com.example.taskplanner.model.Task task : tasks)
            @if(task.getParentTask() == null || ((selectedCategory != null && task.getCategory() == selectedCategory) || (selectedTag != null && task.getTags().contains(selectedTag))))
                <div class="col-md-4 mb-4">
                    <div class="card">
                        <div class="card-body">
                            <h5 class="card-title"><a href="/tasks/${task.getId()}">${task.getTitle()}</a></h5>
                            @if(task.getParentTask() != null)
                                <p class="card-text">Parent Task: <a href="/tasks/${task.getParentTask().getId()}">${task.getParentTask().getTitle()}</a></p>
                            @endif
                            @if(task.getCategory() != null)
                                <p class="card-text">Category: <a href="/tasks/category/${task.getCategory().getId()}">${task.getCategory().getName()}</a></p>
                            @endif
                            @if(task.getDueDate() != null)
                                <p class="card-text"><small class="text-muted">Due: <span class="due-date" data-due-date="${task.getDueDate().toString()}"></span></small></p>
                            @endif
                            <p class="card-text">${task.getDescription()}</p>
                            <p class="card-text">Completed: ${task.isCompleted() ? "Yes" : "No"}</p>
                            @if(task.getTags() != null && !task.getTags().isEmpty())
                                <p class="card-text">Tags:
                                    @for(com.example.taskplanner.model.Tag tag : task.getTags())
                                        <span class="badge bg-primary"><a href="/tasks/tag/${tag.getId()}" class="text-white">${tag.getName()}</a></span>
                                    @endfor
                                </p>
                            @endif
                            @if(task.getSubTasks() != null && !task.getSubTasks().isEmpty())
                                <p class="card-text">Subtasks: ${task.getSubTasks().size()}</p>
                            @endif
                        </div>
                    </div>
                </div>
            @endif
        @endfor
    </div>

    <script>
        document.addEventListener("DOMContentLoaded", function() {
            document.querySelectorAll('.due-date').forEach(function(element) {
                const dueDate = new Date(element.getAttribute('data-due-date'));
                element.textContent = dueDate.toLocaleString();
            });
        });
    </script>

`)