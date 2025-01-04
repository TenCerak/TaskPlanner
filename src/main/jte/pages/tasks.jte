@param java.util.List<com.example.taskplanner.model.Task> tasks
@template.layout(title="Task Planner - View Tasks", content=@`

    <h1 class="text-center my-4">View Tasks</h1>

    <div class="row">
        <a href="/tasks/new" class="btn btn-primary">Add Task</a>
        @for(com.example.taskplanner.model.Task task : tasks)
            @if(task.getParentTask() == null)
                <div class="col-md-4 mb-4">
                    <div class="card">
                        <div class="card-body">
                            <h5 class="card-title"><a href="/tasks/${task.getId()}">${task.getTitle()}</a></h5>
                            <p class="card-text">${task.getDescription()}</p>
                            @if(task.getDueDate() != null)
                                <p class="card-text"><small class="text-muted">Due: <span class="due-date" data-due-date="${task.getDueDate().toString()}"></span></small></p>
                            @endif
                            <p class="card-text">Completed: ${task.isCompleted() ? "Yes" : "No"}</p>
                            @if(task.getTags() != null && !task.getTags().isEmpty())
                                <p class="card-text">Tags:
                                    @for(com.example.taskplanner.model.Tag tag : task.getTags())
                                        <span class="badge bg-primary">${tag.getName()}</span>
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