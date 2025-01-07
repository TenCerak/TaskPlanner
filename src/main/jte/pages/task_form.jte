@param com.example.taskplanner.model.Task task
@param java.util.List<com.example.taskplanner.model.Category> categories
@template.layout(title="Task Planner - Create Task", content=@`

    <h1 class="text-center my-4">Create Task</h1>

    <form action="/tasks" method="post">
        <div class="form-group">
            <label for="title">Title</label>
            <input type="text" class="form-control" id="title" name="title" required>
        </div>
        <div class="form-group">
            <label for="category">Category</label>
            <select class="form-control" id="category" name="categoryId">
                <option value="">Select a category</option>
                @for(com.example.taskplanner.model.Category category : categories)
                    <option value="${category.getId()}">${category.getName()}</option>
                @endfor
            </select>
        </div>
        <div class="form-group">
            <label for="description">Description</label>
            <textarea class="form-control" id="description" name="description" rows="3"></textarea>
        </div>
        <div class="form-group">
            <label for="dueDate">Due Date</label>
            <input type="datetime-local" class="form-control" id="dueDate" name="dueDate">
        </div>
        @if(task.getParentTask() != null)

            <input type="hidden" id="parentTaskId" name="parentTaskId" value="${task.getParentTask().getId()}">
        @endif
        <button type="submit" class="btn btn-primary">Create Task</button>
    </form>

`)