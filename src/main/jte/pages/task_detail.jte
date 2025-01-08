@param com.example.taskplanner.model.Task task
@param java.util.List<com.example.taskplanner.model.Tag> tags
@template.layout(title="Task Planner - Task Detail", content=@`

    <h1 class="text-center my-4">Task Detail</h1>

    <div class="card">
        <div class="card-body">
            <h5 class="card-title">${task.getTitle()}</h5>
            <p class="card-text">${task.getDescription()}</p>
            @if(task.getDueDate() != null)
                <p class="card-text"><small class="text-muted">Due: <span class="due-date" data-due-date="${task.getDueDate().toString()}"></span></small></p>
            @endif
            @if(task.getCategory() != null)
                <p class="card-text">Category: <a href="/tasks/category/${task.getCategory().getId()}">${task.getCategory().getName()}</a></p>
            @endif
            <p class="card-text">Completed: ${task.isCompleted() ? "Yes" : "No"}</p>
            @if(task.getTags() != null && !task.getTags().isEmpty())
                <p class="card-text">Tags:</p>
                    @for(com.example.taskplanner.model.Tag tag : task.getTags())
                        <span class="badge bg-primary d-inline-flex align-items-center">
                            <a href="/tasks/tag/${tag.getId()}" class="text-white">${tag.getName()}</a>
                            <form action="/tasks/${task.getId()}/remove-tag" method="post" style="display:inline;">
                                <input type="hidden" name="tagId" value="${tag.getId()}">
                                <button type="submit" class="btn-close btn-close-white ms-2" aria-label="Remove"></button>
                            </form>
                        </span>
                    @endfor
            @endif
            <form action="/tasks/${task.getId()}/add-tag" method="post" class="mb-4">
                <div class="input-group">
                    <select name="tagId" class="form-control" id="tagSelect">
                        <option value="">Select a tag</option>
                        @for(com.example.taskplanner.model.Tag tag : tags)
                            <option value="${tag.getId()}">${tag.getName()}</option>
                        @endfor
                    </select>
                    <button type="submit" class="btn btn-primary" id="addTagButton" disabled>Add Tag</button>
                </div>
            </form>
            @if(task.getSubTasks() != null && !task.getSubTasks().isEmpty())
                <h6>Subtasks:</h6>
                <div class="row">
                    @for(com.example.taskplanner.model.Task subtask : task.getSubTasks())
                        <div class="col-md-4 mb-4">
                            <div class="card">
                                <div class="card-body">
                                    <h5 class="card-title"><a href="/tasks/${subtask.getId()}">${subtask.getTitle()}</a></h5>
                                    <p class="card-text">${subtask.getDescription()}</p>
                                    @if(subtask.getDueDate() != null)
                                        <p class="card-text"><small class="text-muted">Due: <span class="due-date" data-due-date="${subtask.getDueDate().toString()}"></span></small></p>
                                    @endif
                                    @if(subtask.getCategory() != null)
                                        <p class="card-text">Category: <a href="/tasks/category/${subtask.getCategory().getId()}">${subtask.getCategory().getName()}</a></p>
                                    @endif
                                    <p class="card-text">Completed: ${subtask.isCompleted() ? "Yes" : "No"}</p>
                                    @if(subtask.getTags() != null && !subtask.getTags().isEmpty())
                                        <p class="card-text">Tags:
                                            @for(com.example.taskplanner.model.Tag tag : subtask.getTags())
                                                <span class="badge bg-primary"><a href="/tasks/tag/${tag.getId()}" class="text-white">${tag.getName()}</a></span>
                                            @endfor
                                        </p>
                                    @endif
                                    @if(subtask.getSubTasks() != null && !subtask.getSubTasks().isEmpty())
                                        <p class="card-text">Subtasks: ${subtask.getSubTasks().size()}</p>
                                    @endif
                                </div>
                            </div>
                        </div>
                    @endfor
                </div>
            @else
                <p>No subtasks available.</p>
            @endif

            <div class="mt-4">
                <a href="/tasks" class="btn btn-primary">Back to Tasks</a>
                @if(task.getParentTask() != null)
                    <a href="/tasks/${task.getParentTask().getId()}" class="btn btn-secondary">Back to Parent Task</a>
                @endif
                <form action="/tasks/${task.getId()}/toggle-complete" method="post" style="display:inline;">
                    <button type="submit" class="btn btn-success" disabled="${!task.AllSubtasksCompleted()}" title="All subtasks must be completed">
                        ${task.isCompleted() ? "Mark as Incomplete" : "Complete Task"}
                    </button>
                </form>
                <form action="/tasks/${task.getId()}/delete" method="post" style="display:inline;">
                    <button type="submit" class="btn btn-danger">Delete Task</button>
                </form>
                <a href="/tasks/new?parentId=${task.getId()}" class="btn btn-secondary">Add Subtask</a>
            </div>
        </div>
    </div>

    <script>
        document.addEventListener("DOMContentLoaded", function() {
            document.querySelectorAll('.due-date').forEach(function(element) {
                const dueDate = new Date(element.getAttribute('data-due-date'));
                element.textContent = dueDate.toLocaleString();
            });

            const tagSelect = document.getElementById('tagSelect');
            const addTagButton = document.getElementById('addTagButton');

            tagSelect.addEventListener('change', function() {
                addTagButton.disabled = !tagSelect.value;
            });
        });
    </script>

`)